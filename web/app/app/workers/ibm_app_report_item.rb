module Ibmapp
  # This class represents each of the /ScanGroup/Scan/ReportItems/ReportItem
  # elements in the Acunetix XML document.
  #
  # It provides a convenient way to access the information scattered all over
  # the XML in attributes and nested tags.
  #
  # Instead of providing separate methods for each supported property we rely
  # on Ruby's #method_missing to do most of the work.
  class ReportItem
    attr_accessor :xml

    # Accepts an XML node from Nokogiri::XML.
    def initialize(xml_node)
      @xml = xml_node
    end

    # List of supported tags. They can be attributes, simple descendans or
    # collections.
    def supported_tags
      [
        # attributes
        # :color

        # simple tags
        "issue-type".to_sym, :severity,"cvss-score".to_sym, :remediation, "fix-recommendation".to_sym,
        :advisory,
        ]
    end

    # This allows external callers (and specs) to check for implemented
    # properties
    def respond_to?(method, include_private=false)
      return true if supported_tags.include?(method.to_sym)
      super
    end

    # This method is invoked by Ruby when a method that is not defined in this
    # instance is called.
    #
    # In our case we inspect the @method@ parameter and try to find the
    # attribute, simple descendent or collection that it maps to in the XML
    # tree.
    def method_missing(method, *args)

      # We could remove this check and return nil for any non-recognized tag.
      # The problem would be that it would make tricky to debug problems with
      # typos. For instance: <>.potr would return nil instead of raising an
      # exception
      unless supported_tags.include?(method)
        super
        return
      end

      # then we try the children tags
      tag = xml.at_xpath("./#{method}")
      if tag && !tag.text.blank?
        if tags_with_html_content.include?(method)
          return cleanup_html(tag.text)
        elsif tags_with_commas.include?(method)
          return cleanup_decimals(tag.text)
        else
          return tag.text
        end
      else
        'n/a'
      end

      return 'unimplemented'   if method == :cve_list

      # nothing found
      return nil
    end

    private

    def cleanup_html(source)
      result = source.dup
      result.gsub!(/&quot;/, '"')
      result.gsub!(/&amp;/, '&')
      result.gsub!(/&lt;/, '<')
      result.gsub!(/&gt;/, '>')

      result.gsub!(/<b>(.*?)<\/b>/) { "*#{$1.strip}*" }
      result.gsub!(/<br\/>/, "\n")
      result.gsub!(/<font.*?>(.*?)<\/font>/m, '\1')
      result.gsub!(/<h2>(.*?)<\/h2>/) { "*#{$1.strip}*" }
      result.gsub!(/<i>(.*?)<\/i>/, '\1')
      result.gsub!(/<p>(.*?)<\/p>/, '\1')
      result.gsub!(/<code><pre.*?>(.*?)<\/pre><\/code>/m){|m| "\n\nbc.. #{$1.strip}\n\np.  \n" }
      result.gsub!(/<pre.*?>(.*?)<\/pre>/m){|m| "\n\nbc.. #{$1.strip}\n\np.  \n" }
      result.gsub!(/<ul>(.*?)<\/ul>/m, '\1')

      result.gsub!(/<li>(.*)?<\/li>/, '* \1')

      result
    end

    def cleanup_decimals(source)
      result = source.dup
      result.gsub!(/([0-9])\,([0-9])/, '\1.\2')
      result
    end

    # Some of the values have embedded HTML conent that we need to strip
    def tags_with_html_content
      [:details, :description, :detailed_information, :impact]
    end

    def tags_with_commas
      [:cvss3_score, :cvss3_tempscore, :cvss3_envscore]
    end

  end
end
