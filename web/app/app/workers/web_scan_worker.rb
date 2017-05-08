class WebScanWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	def perform(scan_id,scaner_instance)
		begin
		scan = Scaner.find(scan_id)
		target = scan.target	
		scan.update_scan_status("Scanning","Scanning started on #{Time.now.to_formatted_s(:short)}")
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		@logfile = File.open(Rails.root.join("log/scans/#{scan.id}.log"), 'a')
		pipeline_options = {
			:appname => scan.project_title,
			:quiet => true,
			:run_tasks => [],
			:scan_id=> scan.id,
			:logfile => @logfile
		}
		pipeline_options[:target] = scan.repo.ssh_repo_url

		#pipeline_options[:run_tasks] << Setting.pipeline['tasks_for']['web_scans'].split(",")
		pipeline_options[:run_tasks] = ["ArachniScanner"]
		findings = Array.new
		if pipeline_options[:run_tasks].count > 0
			pipeline_thread = Thread.new do
				tracker = Pipeline.run(pipeline_options)
				findings << tracker.findings
			end
			[pipeline_thread].each {|t| t.join}

			#scan.save_findings(findings,scaner_instance) if findings.count > 0
		end
	#	if scan.repo.ssh_repo_url.include?("http")
	#		save_path = scan.repo.ssh_repo_url.split("http://").last.split(".").first
	#	else
	#		save_path = scan.repo.ssh_repo_url.split("https://").last.split(".").first
	#	end
	#	report_path = File.join(Rails.root,"public","arachni_reports")
	#	result_xml_path = File.join(report_path,save_path+ '.xml')
	#	ArachniUploadWorker.new.perform(scan_id,result_xml_path)
		rescue Exception=>e
			@logfile.puts "Error occurred while running webscan ...#{e.inspect}"
		end
	ensure
		scan = Scaner.find(scan_id)
		scan.update_scan_status("done","completed on #{Time.now.to_formatted_s(:short)}")
		@logfile.puts "sending notification mail..for scan id.#{scan.id}"
		scan.send_notifications
		@logfile.close	
	end

end
