class NexposeUploadWorker 
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
		if @doc.root.name == 'NeXposeSimpleXML'
			process_simple
		elsif @doc.root.name == 'NexposeReport'
			process_full
			update_sev_count
		else
			@scan.update_scan_status("failed","The document doesn't seem to be in either NeXpose-Simple or NeXpose-Full XML format")
		end
	end
	def process_full
		@doc.xpath('//VulnerabilityDefinitions/vulnerability').each do |xml_vulnerability|
			issue_text = process_template(template: 'full_vulnerability', data: xml_vulnerability,plugin: 'nexpose')
			file_out_put = issue_text.scan(/#\[(.+?)\]#[\r|\n](.*?)(?=#\[|\z)/m)
			finding_hash = Hash.new
			file_out_put.each do |out_put|
				finding_hash[out_put[0]] = out_put[1]
			end
			case finding_hash["Severity"].gsub(/[^a-zA-Z0-9\-]/,"").to_i
			when 0..0.0
				severity = 'Info'
			when 0.1..3.9 
				severity = 'Low'
			when 4.0..6.9
				severity = 'Medium'
			when 7.0..9.9
				severity = 'High'
			when 10..10.0
				severity = 'Critical'
			else
				severity = 'Weak'
			end
			save_finding(bug_type: finding_hash["Title"],severity: severity,scanner: 'Nexpose',cvss_score: finding_hash["CVSS Score"],detail: finding_hash["Description"],external_link: finding_hash["References"],solution: finding_hash["Solution"])
		end
	end
	def process_simple

	end
end
