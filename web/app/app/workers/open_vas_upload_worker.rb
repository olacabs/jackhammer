class OpenVASUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	sidekiq_options unique: :while_executing
	attr_accessor :logger, :template, :templates_dir,:plugin
	def perform(scan,file)
		@scan = Scaner.find(scan)
		 if file.present?
                        file_content = File.read(file)
                else
                        file_content = File.read(@scan.project_target.path)
                end    
		@doc = Nokogiri::XML( file_content )
		@total_count,@critical_count,@high_count,@medium_count,@low_count,@info_count = 0,0,0,0,0,0
		if @doc.xpath('/report').empty?
			@scan.update_scan_status("failed","No scan results in the uploaded file, Ensure you uploaded a OpenVAS report")
		else
			@doc.xpath('/report/report/results/result').each do |xml_result|
				process_result(xml_result)
			end	
			update_sev_count
		end
	end
	def process_result(xml_result)
		issue_text = process_template(template: 'result', data: xml_result,plugin: 'openvas')
		port_info = xml_result.at_xpath('./port').text
		file_out_put = issue_text.scan(/#\[(.+?)\]#[\r|\n](.*?)(?=#\[|\z)/m)
		finding_hash = Hash.new
		file_out_put.each do |out_put|
			finding_hash[out_put[0]] = out_put[1]
		end
		save_finding(bug_type: finding_hash["Title"],severity: finding_hash["Threat"].split(" ").first.gsub(/[^a-zA-Z0-9\-]/,"").capitalize,scanner: 'OpenVAS',cvss_score: finding_hash["CVSSv2"],detail: finding_hash["RawDescription"],external_link: finding_hash["References"],solution: finding_hash["Recommendation"],confidence: finding_hash["Confidence"])
	end
end
