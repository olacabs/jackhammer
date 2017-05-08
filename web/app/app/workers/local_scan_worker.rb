class LocalScanWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include Base
        sidekiq_options({ :queue => :default,:retry => false })
	def perform(scan_id,scaner_instance_id)
		@scan = Scaner.find(scan_id)
		scaner_instance = ScanerInstance.find(scaner_instance_id)
		@scan.start_time = Time.now.asctime
		@scan.update_scan_status("Scanning","Started scanning on #{Base.current_time}")
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		@logfile = File.open(Rails.root.join("log/scans/#{@scan.id}.log"), 'a')
		copied_dir = untar_project
		project_langs = Base.code_lang(copied_dir)
                @scan.found_langs = project_langs.join(",")
		is_supported = Base.is_language_supported(project_langs)
		if is_supported
			Base.start_tasks(@scan.project_target_file_name,copied_dir,@scan,@logfile,scaner_instance)
		else
			Base.no_support_lang_logs(@logfile,@scan,project_langs)
		end
	ensure
		@scan.end_time = Time.now.asctime
		@scan.save
	end

	def untar_project
		dir = Dir.mktmpdir
		filename = Dir::Tmpname.make_tmpname(['jackhammer', '.tar.gz'], nil)
		archive_link = @scan.project_target.path
		IO.copy_stream(open(archive_link), "#{dir}/#{filename}")
		raise(IOError, "#{dir}/#{filename} does not exist") unless FileTest.exists?("#{dir}/#{filename}")
		exit_msg = `tar xzf #{dir}/#{filename} -C #{dir} 2>&1`
		raise(IOError, exit_msg) if $?.to_i != 0
		return dir
	rescue StandardError => e
		Rails.logger.error "Error inside_gitlab_repo #{e.message}" unless Rails.env == 'test'
		raise e
	end

end
