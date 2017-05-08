require 'nmap/xml'
class FortifyUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	attr_accessor :logger, :template, :templates_dir,:plugin
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
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		@logfile = File.open(Rails.root.join("log/scans/#{@scan.id}.log"), 'a')
		if @doc.xpath('/ReportDefinition').empty?
			@scan.update_scan_status("failed","The document doesn't seem fortify XML format")
		else
			@total_count,@critical_count,@high_count,@medium_count,@low_count,@info_count = 0,0,0,0,0,0
			@doc.xpath('/ReportDefinition/ReportSection').each do |report_section|
				process_report_section(report_section)
			end
			update_sev_count
		end

	end
	def process_report_section(report_section)
		begin
			if report_section.xpath("SubSection/IssueListing/Chart/GroupingSection/Issue").present?
				report_section.xpath("SubSection/IssueListing/Chart/GroupingSection/Issue").each do |each_issue|
					process_issue(each_issue)
				end
			end
		rescue Exception=>e
			@logfile.puts "Report Item is invalid"
		end
	end
	def process_issue(xml_report_item)
		category = xml_report_item.xpath("Category").text
		severity = xml_report_item.xpath("Friority").text
		file_path = xml_report_item.xpath("Primary/FilePath").text
		line_no = xml_report_item.xpath("Primary/LineStart").text
		code = xml_report_item.xpath("Primary/Snippet").text
		description = xml_report_item.xpath("Abstract").text
		save_finding(bug_type: category,severity: severity,scanner: 'Fortify',detail: description,code: code,line: line_no,file: file_path) 
	end
end
