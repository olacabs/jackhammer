class MobileScanWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	def perform(scan_id,scaner_instance)
		begin
		scan = Scaner.find(scan_id)
		target = scan.project_target.path	
		scan.update_scan_status("Scanning","Scanning started on #{Time.now.to_formatted_s(:short)}")
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		@logfile = File.open(Rails.root.join("log/scans/#{scan.id}.log"), 'a')
		pipeline_options = {
			:appname => scan.project_title,
			:quiet => true,
			:run_tasks => [],
			:logfile => @logfile
		}
		pipeline_options[:target] = target

		#pipeline_options[:run_tasks] << Setting.pipeline['tasks_for']['web_scans'].split(",")
		pipeline_options[:run_tasks] = ["Androguard"]
		findings = Array.new
		if pipeline_options[:run_tasks].count > 0
			pipeline_thread = Thread.new do
				tracker = Pipeline.run(pipeline_options)
				findings << tracker.findings
			end
			[pipeline_thread].each {|t| t.join}
			puts "====findings===#{findings.inspect}"
			scan.save_findings(findings,scaner_instance) if findings.count > 0
		end
		
		
		rescue Exception=>e
			@logfile.puts "Error occurred while running dynamic scans ...#{e.backtrace}"
		end
	ensure
		scan = Scaner.find(scan_id)
		scan.update_scan_status("done","completed on #{Time.now.to_formatted_s(:short)}")
		@logfile.puts "sending notification mail..for scan id.#{scan.id}"
		scan.send_notifications
		@logfile.close	
	end

end
