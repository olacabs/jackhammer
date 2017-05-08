class ZapUploadWorker 
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
		if @doc.xpath('/OWASPZAPReport/site').empty?
			@scan.update_scan_status("failed","No scan results in the uploaded file, Ensure you uploaded an ZAP XML report")
		else
			@doc.xpath('/OWASPZAPReport/site').each do |xml_site|
				process_site(xml_site)
			end	
			update_sev_count
		end
	end
	def process_site(xml_site)
		xml_site.xpath('./alerts/alertitem').each do |xml_alert_item|
			process_alert_item(xml_alert_item)
		end
	end
	def process_alert_item(alert_item)
		issue_text = process_template(template: 'issue', data: alert_item,plugin: 'zap')
		file_out_put = issue_text.scan(/#\[(.+?)\]#[\r|\n](.*?)(?=#\[|\z)/m)
		finding_hash = Hash.new
		file_out_put.each do |out_put|
			finding_hash[out_put[0]] = out_put[1]
		end
		save_finding(bug_type: finding_hash["Title"],severity: finding_hash["Risk"].split(" ").first.gsub(/[^a-zA-Z0-9\-]/,"").capitalize,scanner: 'Zap',cvss_score: finding_hash["CVSSScore"],detail: finding_hash["Description"],external_link: finding_hash["References"],solution: finding_hash["Solution"],confidence: finding_hash["Confidence"])
	end
end
