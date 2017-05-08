class NessusUploadWorker 
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
		if @doc.xpath('/NessusClientData_v2/Report').empty?
			@scan.update_scan_status("failed","No scan results in the uploaded file, Ensure you uploaded a Nessus XML v2 report")
		else
			@doc.xpath('/NessusClientData_v2/Report').each do |xml_report|
				xml_report.xpath('./ReportHost').each do |xml_host|
					process_report_host(xml_host)
				end
			end	
			update_sev_count
		end
	end
	def process_report_host(xml_host)
		xml_host.xpath('./ReportItem').each do |xml_report_item|
			next if xml_report_item.attributes['pluginID'].value == "0"
			process_report_item(xml_host, xml_report_item)
		end
	end
	def process_report_item(xml_host, xml_report_item)
		issue_text = process_template(template: 'report_item', data: xml_report_item,plugin: 'nessus')
		issue_text << "\n\n#[Host]#\n#{xml_host.attributes['name']}\n\n"
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
		save_finding(bug_type: finding_hash["Title"],severity: severity,scanner: 'Nessus',cvss_score: finding_hash["CVE Entries"],detail: finding_hash["Description"],external_link: finding_hash["See also"],solution: finding_hash["Solution"])
	end
end
