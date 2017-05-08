class IBMAppUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	sidekiq_options unique: :while_executing
	def perform(scan,file=nil)
		#file_content    = File.read(scaner_params[:project_target].tempfile)
		@scan = Scaner.find(scan)
		if file.present?
                        file_content = File.read(file)
                else
                        file_content = File.read(@scan.project_target.path)
                end
		@doc = Nokogiri::XML( file_content )
		if @doc.xpath('/xml-report/issue-group').empty?
			@scan.update_scan_status("failed","No scan results/Invalid file format,make sure your uploading IBM App scan results")
		else
			@total_count,@critical_count,@high_count,@medium_count,@low_count,@info_count = 0,0,0,0,0,0
			@doc.xpath('/xml-report/issue-group/item').each do |each_item|
				process_report_item(each_item)
			end
			update_sev_count
		end

	end
	def process_report_item(xml_report_item)
		issue_text = process_template(template: 'report_item', data: xml_report_item,plugin: 'ibmapp')
		file_out_put = issue_text.scan(/#\[(.+?)\]#[\r|\n](.*?)(?=#\[|\z)/m)
		finding_hash = Hash.new
		file_out_put.each do |out_put|
			finding_hash[out_put[0]] = out_put[1]
		end
		finding_hash["Severity"] = 'Info' if finding_hash["Severity"].gsub(/[^a-zA-Z0-9\-]/,"").downcase == "informational"
		sev = finding_hash["Severity"].gsub(/[^a-zA-Z0-9\-]/,"").capitalize
		save_finding(bug_type: finding_hash["Title"],severity: sev,scanner: 'IBMApp',cvss_score: finding_hash["CVSSScore"],detail: finding_hash["DetailedInformation"],references: finding_hash["References"])
	end
end
