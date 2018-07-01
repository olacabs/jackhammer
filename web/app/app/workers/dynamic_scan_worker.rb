class DynamicScanWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	sidekiq_options({ :queue => :dynamic,:retry => 2 })
	def perform(scan_id,scaner_instance)
		begin
			scan = Scaner.find(scan_id)
			scaner_instance = ScanerInstance.find(scaner_instance)
			scan.start_time = Time.now.asctime
			scan.save
			scan.update_scan_status("Scanning","Scanning started on #{Time.now.to_formatted_s(:short)}")
			@project_title = scan.project_title.present? ? scan.project_title : scan.repo.name
			AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'success',message: "#{scan.scan_type} Scanning started..",scaner_id: scan.id,task_name: @project_title)
			Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
			@logfile = File.open(Rails.root.join("log/scans/#{scan.id}.log"), 'a')
			pipeline_options = {
				:appname => scan.project_title,
				:quiet => true,
				:scan_id=> scan.id,
				:run_tasks => [],
				:logfile => @logfile
			}
			pipeline_options[:target] = scan.target
			
			scan_type = ScanType.where("name like ?", "%#{scan.scan_type}%")
			tools = scan_type.first.tasks.map(&:name)
			pipeline_tools = Setting.find_by(var: 'pipeline')
			if pipeline_tools.present?
				pipeline_tools = Setting.find_by(var: 'pipeline').value
				pipeline_tools = pipeline_tools['enabled_tools']
			end
			pipeline_tools = Setting.pipeline['enabled_tools'] unless pipeline_tools.present?
			if pipeline_tools.present?
				pipeline_options[:run_tasks] = tools  & pipeline_tools.split(",")
			else
				pipeline_options[:run_tasks]  = []
			end
			findings = Array.new
			if pipeline_options[:run_tasks].count > 0
				Timeout::timeout(2.hours) { 
					pipeline_thread = Thread.new do
						tracker = Pipeline.run(pipeline_options)
						findings << tracker.findings
					end
					[pipeline_thread].each {|t| t.join}
				}
			end
			scan.save_findings(findings,scaner_instance) if findings.count >0
			@logfile.close
		rescue Exception=>e
			@logfile = File.open(Rails.root.join("log/scans/#{scan_id}.log"), 'a')
			scan.update_scan_status("failed","Some error occured")
			scan.end_time = Time.now.asctime
			scan.save
			#FileUtils.remove_entry_secure(@dir) if Dir.exist?(@dir)
			AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: "#{scan.scan_type} Scanning failed",scaner_id: scan.id,task_name: @project_title)
			@logfile.puts "Error occurred while running dynamicscan ...#{e.backtrace}"
			@logfile.close
		end
	ensure
		scan = Scaner.find(scan_id)
		@logfile = File.open(Rails.root.join("log/scans/#{scan_id}.log"), 'a')
		scan.update_scan_status("Completed","completed")
		scan.end_time = Time.now.asctime
		scan.save
		#FileUtils.remove_entry_secure(@dir) if Dir.exist?(@dir)
		AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'success',message: "#{scan.scan_type} Scanning completed",scaner_id: scan.id,task_name: @project_title)
		@logfile.puts "sending notification mail..for scan id.#{scan_id}"
		#scan.send_notifications
		@logfile.close	
		if scan.project_target.present?
			scan.project_target.destroy
			scan.project_target.clear
			scan.update(project_target: nil)
			scan.project_target.save
		end
		scan.send_notifications
	end

end
