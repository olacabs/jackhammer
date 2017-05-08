module Openvas::V7

  # The format is given by the OMPv5 :get_reports call (OpenVASv7 uses OMPv5).
  #
  # See:
  #   http://www.openvas.org/omp-5-0.html#command_get_reports
  class Result < ::Openvas::ReportItem


    private
    # This method parses the <tags> tag of the <result> entry and extracts the
    # available fields. Previous versions of the format (e.g. OpenVAS v6)
    # included these embedded fields in the <description> tag instead of the
    # <tags> tag, hence the not-so-intuitive name.
    def description_fields
      if @tag_fields.nil?
        delimiters = {
          # Not supported via .fields
          # 'cvss_base_vector='
          'impact=' => :impact,

          # Not supported via .fields
          # 'vuldetect='
          'insight=' => :insight,
          'solution=' => :solution,
          'summary=' => :summary,
          'affected=' => :affected_software

          # Missing fields, these used to be available under <description> but it
          # doesn't look like they are under <tags>
          # 'Impact Level:' => :impact_level,
          # 'Information that was gathered:' => :info_gathered,
        }

        @tag_fields = {}
        current_field = nil
        buffer = ''
        clean_line = nil

        @xml.at_xpath('./nvt/tags').text().split('|').each do |tag_line|
          clean_line = tag_line.lstrip

          if clean_line.empty?
            buffer << "\n"
            next
          end

          delimiters.keys.each do |tag_name|
            if tag_line.starts_with?(tag_name)
              @tag_fields[delimiters[tag_name]] = clean_line[tag_name.length..-1]
              next
            end
          end
        end
      end

      @tag_fields
    end

  end
end
