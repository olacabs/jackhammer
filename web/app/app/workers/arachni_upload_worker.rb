class ArachniUploadWorker 
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
		if !@doc.root.present? || @doc.root.name!='report'
			@scan.update_scan_status("failed","The document doesn't seem Arachni XML format")
		else
			@doc.xpath("./report/issues/issue").each do |xml_item|
				process_report_item(xml_item)
			end
			update_sev_count
		end
	end
	def process_report_item(xml_item)
		issue_text = process_template(template: 'report_item', data: xml_item,plugin: 'arachni')			
		file_out_put = issue_text.scan(/#\[(.+?)\]#[\r|\n](.*?)(?=#\[|\z)/m)
		finding_hash = Hash.new
		file_out_put.each do |out_put|
			finding_hash[out_put[0]] = out_put[1]
		end
		finding_hash["Severity"] = 'Info' if finding_hash["Severity"].gsub(/[^a-zA-Z0-9\-]/,"").downcase == "informational"
		save_finding(bug_type: finding_hash["Title"],severity: finding_hash["Severity"].gsub(/[^a-zA-Z0-9\-]/,"").capitalize,scanner: 'Arachni',cvss_score: finding_hash["CVSSScore"],detail: finding_hash["Description"],external_link: finding_hash["References"],solution: finding_hash["RemedyGuidance"],request: finding_hash["Request"],response: finding_hash["Response"])
	end
end
