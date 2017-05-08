class AcunetixUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	attr_accessor :logger, :template, :templates_dir,:plugin
	sidekiq_options unique: :while_executing
	def perform(scan)
		#file_content    = File.read(scaner_params[:project_target].tempfile)
		@scan = Scaner.find(scan,file=nil)
		if file.present?
			file_content = File.read(file)
		else 
			file_content = File.read(@scan.project_target.path)
		end 
		@doc = Nokogiri::XML( file_content )
		if @doc.xpath('/ScanGroup/Scan').empty?
			@scan.update_scan_status("failed","No scan results were detected in the uploaded file")
		else
			@total_count,@critical_count,@high_count,@medium_count,@low_count,@info_count = 0,0,0,0,0,0
			@doc.xpath('/ScanGroup/Scan').each do |xml_scan|
				process_scan(xml_scan)
			end
			update_sev_count
		end

	end
	def process_scan(xml_scan)
		xml_scan.xpath('./ReportItems/ReportItem').each do |xml_report_item|
			process_report_item(xml_report_item)
		end
	end
	def process_report_item(xml_report_item)
		issue_text = process_template(template: 'report_item', data: xml_report_item,plugin: 'acunetix')
		file_out_put = issue_text.scan(/#\[(.+?)\]#[\r|\n](.*?)(?=#\[|\z)/m)
		finding_hash = Hash.new
		file_out_put.each do |out_put|
			finding_hash[out_put[0]] = out_put[1]
		end
		sev = finding_hash["Severity"].gsub(/[^a-zA-Z0-9\-]/,"").capitalize
		save_finding(bug_type: finding_hash["Title"],severity: sev,scanner: 'Acunetix',cvss_score: finding_hash["CVSSScore"],detail: finding_hash["DetailedInformation"],references: finding_hash["References"])
	end
end
