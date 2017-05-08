class BurpUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	sidekiq_options unique: :while_executing
	attr_accessor :logger, :template, :templates_dir,:plugin
	def perform(scan,file=nil)
		#file_content    = File.read(scaner_params[:project_target].tempfile)
		@scan = Scaner.find(scan)
		if file.present?
			file_content = File.read(file)
		else
			file_content = File.read(@scan.project_target.path)
		end
		doc = Nokogiri::XML( file_content )
		@total_count,@critical_count,@high_count,@medium_count,@low_count,@info_count = 0,0,0,0,0,0
		if doc.root.name != 'issues'
			@scan.update_scan_status("failed","Document doesn't seem to be in the Burp Scanner XML format.")
		else
			doc.xpath('issues/issue').each do |xml_issue|
				issue_text = process_template(template: 'issue', data: xml_issue,plugin: 'burp')
				file_out_put = issue_text.scan(/#\[(.+?)\]#[\r|\n](.*?)(?=#\[|\z)/m)
				finding_hash = Hash.new
				file_out_put.each do |out_put|
					finding_hash[out_put[0]] = out_put[1]
				end
				finding_hash["Severity"] = 'Info' if finding_hash["Severity"].gsub(/[^a-zA-Z0-9\-]/,"").downcase == 'Information'
				save_finding(bug_type: finding_hash["Title"],severity: finding_hash["Severity"],scanner: 'Burp',cvss_score: finding_hash["CVSSScore"],detail: finding_hash["Detail"],external_link: finding_hash["References"],solution: finding_hash["RemediationDetails"],location: finding_hash["Location"],file: finding_hash["Path"],host: finding_hash["Host"],request: finding_hash["Request"],response: finding_hash["Response"],confidence: finding_hash["Confidence"])
			end
			update_sev_count
		end
	end
end
