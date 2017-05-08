class QualysUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	sidekiq_options unique: :while_executing
	attr_accessor :logger, :template, :templates_dir,:plugin
	def perform(scan,file=nil)
		@scan = Scaner.find(scan)
		 if file.present?
                        file_content = File.read(file)
                else
                        file_content = File.read(@scan.project_target.path)
                end 
		@doc = Nokogiri::XML( file_content )
		@total_count,@critical_count,@high_count,@medium_count,@low_count,@info_count = 0,0,0,0,0,0
		if @doc.root.name != 'SCAN'
			@scan.update_scan_status("failed","No scan results in the uploaded file, Ensure you uploaded a Qualys report")
		else
			@doc.xpath('SCAN/IP').each do |xml_host|
				process_ip(xml_host)
			end	
			update_sev_count
		end
	end
	def process_ip(xml_host)
		xml_host.xpath('VULNS/CAT').each do |xml_cat|
			empty_dup_xml_cat = xml_cat.dup
			empty_dup_xml_cat.children.remove
			xml_cat.xpath('VULN').each do |xml_vuln|
				vuln_number = xml_vuln[:number]
				# We need to clear any siblings before or after this VULN
				#dup_xml_cat = empty_dup_xml_cat.dup
				#dup_xml_cat.add_child(xml_vuln.dup)
				process_vuln(vuln_number, xml_vuln)
			end
		end
	end
	def process_vuln(vuln_number, xml_cat)
		issue_text = process_template(template: 'element', data: xml_cat,plugin: 'qualys')
		issue_text << "\n\n#[Number]#\n#{ vuln_number }\n\n"
		file_out_put = issue_text.scan(/#\[(.+?)\]#[\r|\n](.*?)(?=#\[|\z)/m)
		finding_hash = Hash.new
		file_out_put.each do |out_put|
			finding_hash[out_put[0]] = out_put[1]
		end
		case finding_hash["Severity"].gsub(/[^a-zA-Z0-9\-]/,"").to_i
                when 1
                        severity = 'Info'
                when 2
                        severity = 'Low'
                when 3
                        severity = 'Medium'
                when 4
                        severity = 'High'
                when 5
                        severity = 'Critical'
                else
                        severity = 'Weak'
                end
		save_finding(bug_type: finding_hash["Title"],severity: severity,scanner: 'Qualys',cvss_score: finding_hash["CVE"],detail: finding_hash["Diagnosis"],external_link: finding_hash["References"],solution: finding_hash["Solution"])		
	end
end
