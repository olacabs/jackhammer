module Openvas
  # This class represents each of the /report/report/results/result elements in
  # the OpenVAS XML document.
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
    # collections (e.g. <bid/>, <cve/>, <xref/>)
    def supported_tags
      [
        # attributes
        # NONE

        # simple tags
        :threat, :description, :original_threat, :notes, :overrides,:severity,

        # nested tags
        :name, :cvss_base, :risk_factor, :cve, :bid, :xref,

        # fields inside :description
        :summary, :info_gathered, :insight, :impact, :impact_level, :affected_software, :solution
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

      # first we try the attributes: port, svc_name, protocol, severity,
      #   plugin_id, plugin_name, plugin_family
      # translations_table = {
      #   # @port           = xml.attributes["port"]
      #   # @svc_name       = xml.attributes["svc_name"]
      #   # @protocol       = xml.attributes["protocol"]
      #   # @severity       = xml.attributes["severity"]
      #   :plugin_id => 'pluginID',
      #   :plugin_name => 'pluginName',
      #   :plugin_family  => 'pluginFamily'
      # }
      # method_name = translations_table.fetch(method, method.to_s)
      # return @xml.attributes[method_name].value if @xml.attributes.key?(method_name)
      method_name = method.to_s


      # first we try the children tags: :threat, :description, :original_threat,
      # :notes, :overrides
      tag = @xml.at_xpath("./#{method_name}")
      if tag
        return tag.text
      end


      # nested tags: :name, :cvss_base, :risk_factor, :cve, :bid, :xref,
      tag = @xml.at_xpath("./nvt/#{method_name}")
      if tag
        return tag.text
      end

      # fields inside :description
      if description_fields.key?(method)
        return description_fields[method]
      end

      # nothing found, the tag is valid but not present in this ReportItem
      return nil
    end

    private
    # This method parses the <description> tag of the <result> entry and
    # extracts the available fields, in a similar way to what Note#fields
    # does.
    def description_fields
      if @description_fields.nil?
        delimiters = {
          'Affected Software/OS:' => :affected_software,
          'Impact:' => :impact,
          'Impact Level:' => :impact_level,
          'Information that was gathered:' => :info_gathered,
          'Solution:' => :solution,
          'Summary:' => :summary,
          'Vulnerability Insight:' => :insight
        }

        @description_fields = {}
        current_field = nil
        buffer = ''
        clean_line = nil

        @xml.at_xpath('./description').text().each_line do |line|
          clean_line = line.lstrip
          if clean_line.empty?
            buffer << "\n"
            next
          end

          # For some reason Impact Level: is followed by the content instead of a new
          # line like the other fields
          if clean_line =~ /Impact Level: (.*)/
            @description_fields[:impact_level] = $1

            # we terminate the previous field and unassign :current_field
            if current_field
              @description_fields[delimiters[current_field]] = buffer
              current_field = nil
              buffer = ''
            end
            next
          end

          if delimiters.key?(clean_line.rstrip)
            if current_field
              # we need the conditional for the 1st iteration
              @description_fields[delimiters[current_field]] = buffer
            end
            current_field = clean_line.rstrip
            buffer = ''
            next
          end

          buffer << clean_line
        end
        # wrap up the last field whose contents are already in the buffer.
        @description_fields[delimiters[current_field]] = buffer
      end

      @description_fields
    end
  end
end


require 'openvas/v6/result'
require 'openvas/v7/result'
