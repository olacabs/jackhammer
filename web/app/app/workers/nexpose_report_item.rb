module Nexpose
  # This class represents each of the /NexposeReport[@version='1.0']/VulnerabilityDefinitions/vulnerability
  # elements in the Nexpose Full XML document.
  #
  # It provides a convenient way to access the information scattered all over
  # the XML in attributes and nested tags.
  #
  # Instead of providing separate methods for each supported property we rely
  # on Ruby's #method_missing to do most of the work.
  class ReportItem
    # Accepts an XML node from Nokogiri::XML.
    def initialize(xml_node)
      @xml = xml_node
    end

    # List of supported tags. They can be attributes, simple descendans or
    # collections (e.g. <references/>, <tags/>)
    def supported_tags
      [
        # attributes
        :nexpose_id, :title, :severity, :pci_severity, :cvss_score, :cvss_vector,
        :published, :added, :modified,

        # simple tags
        :description, :solution,

        # multiple tags
        :references, :tags
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

      # First we try the attributes. In Ruby we use snake_case, but in XML
      # CamelCase is used for some attributes
      translations_table = {
        :nexpose_id => 'id',
        :pci_severity => 'pciSeverity',
        :cvss_score => 'cvssScore',
        :cvss_vector =>'cvssVector'
      }

      method_name = translations_table.fetch(method, method.to_s)
      return @xml.attributes[method_name].value if @xml.attributes.key?(method_name)

      # Then we try simple children tags: description, solution
      tag = @xml.xpath("./#{method_name}/ContainerBlockElement").first
      if tag
        lines = []

        # Go through Paragraphs and extract them.
        # FIXME: we're using .//. to get paragraphs nested in Nexpose lists,
        # ideally we'd convert this lists into Textile bullet point lists.
        tag.xpath(".//Paragraph").each do |xml_paragraph|
          lines << xml_paragraph.text.split("\n").collect(&:strip).join(' ').strip
        end

        return lines.join("\n\n")
      end

      # Finally the enumerations: references, tags
      if method_name == 'references'
        @xml.xpath("./references/reference").collect{|entry| {:source => entry['source'], :text => entry.text} }
      elsif method == 'tags'
        @xml.xpath("./tags/tag").collect(&:text)
      else
        # nothing found, the tag is valid but not present in this ReportItem
        return nil
      end
    end
  end
end
