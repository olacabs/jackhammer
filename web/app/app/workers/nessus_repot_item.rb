module Nessus
  # This class represents each of the /NessusClientData_v2/Report/ReportHost/ReportItem
  # elements in the Nessus XML document.
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
        :port, :svc_name, :protocol, :severity, :plugin_id, :plugin_name, :plugin_family,
        # simple tags
        :solution, :risk_factor, :description, :plugin_publication_date,
        :metasploit_name, :cvss_vector, :cvss_temporal_vector, :synopsis,
        :exploit_available, :patch_publication_date, :plugin_modification_date,
        :cvss_temporal_score, :cvss_base_score, :plugin_output,
        :plugin_version, :exploitability_ease, :vuln_publication_date,
        :exploit_framework_canvas, :exploit_framework_metasploit,
        :exploit_framework_core,
        # multiple tags
        :bid_entries, :cve_entries, :see_also_entries, :xref_entries,
        # compliance tags
        :cm_actual_value, :cm_audit_file, :cm_check_id, :cm_check_name, :cm_info,
        :cm_output, :cm_policy_value, :cm_reference, :cm_result, :cm_see_also,
        :cm_solution
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
      translations_table = {
        # @port           = xml.attributes["port"]
        # @svc_name       = xml.attributes["svc_name"]
        # @protocol       = xml.attributes["protocol"]
        # @severity       = xml.attributes["severity"]
        :plugin_id => 'pluginID',
        :plugin_name => 'pluginName',
        :plugin_family  => 'pluginFamily'
      }
      method_name = translations_table.fetch(method, method.to_s)
      return @xml.attributes[method_name].value if @xml.attributes.key?(method_name)

      # then we try the children tags: solution, risk_factor, description,
      #   plugin_publication_date, metasploit_name, cvss_vector,
      #   cvss_temporal_vector, synopsis, exploit_available,
      #   patch_publication_date, plugin_modification_date, cvss_temporal_score,
      #   cvss_base_score, plugin_output, plugin_version, exploitability_ease,
      #   vuln_publication_date, exploit_framework_canvas,
      #   exploit_framework_metasploit, exploit_framework_core
      tag = @xml.xpath("./#{method_name}").first
      if tag
        return tag.text
      end

      # then the custom XML tags (cm: namespace)
      if method_name.starts_with?('cm_')
        method_name = method_name.sub(/cm_/, 'cm:compliance-').gsub(/_/, '-')
        cm_value = @xml.at_xpath("./#{method_name}", { 'cm' => 'http://www.nessus.org/cm' })
        if cm_value
          return cm_value.text
        else
          return nil
        end
      end


      # finally the enumerations: bid_entries, cve_entries, xref_entries
      translations_table = {
        :bid_entries => 'bid',
        :cve_entries => 'cve',
        :see_also_entries => 'see_also',
        :xref_entries  => 'xref'
      }
      method_name = translations_table.fetch(method, nil)
      if method_name
        @xml.xpath("./#{method_name}").collect(&:text)
      else
        # nothing found, the tag is valid but not present in this ReportItem
        return nil
      end
    end
  end
end
