class GitScanWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include Base
	sidekiq_options unique: :while_executing
	sidekiq_options({ :queue => :static,:retry => 2 })
	#
	#MEMCACHED_POOL = ConnectionPool.new(:size => 10, :timeout => 3) { Dalli::Client.new }
	def perform(scan_id,scaner_instance_id)
		scan = Scaner.find(scan_id)
		@project_title = scan.project_title.present? ? scan.project_title : scan.repo.name
		scaner_instance = ScanerInstance.find(scaner_instance_id)
		scan.start_time = Time.now.asctime
		scan.save
		scan.update_scan_status("Scanning","started Scanning on #{Base.current_time}")
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		@logfile = File.open(Rails.root.join("log/scans/#{scan.id}.log"), 'a')
		repo_url = scan.repo.ssh_repo_url
		repo_url = scan.target unless repo_url.present?
		repo_name = scan.repo.name
		repo_name = scan.project_title unless repo_name.present?
		copied_dir = Base.clone_project(repo_url,scan)
		if !Dir.exist?(copied_dir)
			scan.update_scan_status("Failed","Invalid Repo/Repo can not be accesible")
			AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: "Invalid Repo/Repo can not be accesible",scaner_id: scan.id,task_name: @project_title) 
		else
			project_langs = Base.code_lang(copied_dir)
			scan.found_langs = project_langs.join(",")
			scan.save
			is_supported = Base.is_language_supported(project_langs)
			if is_supported || scan.scan_type == AppConstants::ScanTypes::HARDCODE
				begin
					Timeout::timeout(2.hours) { Base.start_tasks(repo_name,copied_dir,scan,@logfile,scaner_instance) }
				rescue Timeout::Error
					Sidekiq::Retries::Fail.new(RuntimeError.new('whatever happened'))
					scan = Scaner.find(scan_id)
					end_time = Time.now.asctime
					scan.update_scan_status("failed","Time out at #{Base.current_time}")
					scan.update(last_run: Date.today,end_time: end_time)
					AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: "#{scan.scan_type} failed,Timeout",scaner_id: scan.id,task_name: @project_title)
					scan.send_notifications
				end
			else
				Base.no_support_lang_logs(@logfile,scan,project_langs) 
			end
		end
	ensure
		scan = Scaner.find(scan_id)
		scan.end_time = Time.now.asctime
		scan.save
	end
end

