module Base
	require 'pipeline'

	def self.code_lang(dir)

		languages = Array.new
		ruby_files_count = Dir[File.join(dir, '**', '*')].select { |file| File.file?(file) && File.extname(file) == '.rb' }.count
		languages << 'Ruby' if ruby_files_count > 0
		javascript_files_count = Dir[File.join(dir, '**', '*')].select { |file| File.file?(file) && File.extname(file) == '.js' }.count
		languages << 'JavaScript' if javascript_files_count > 0
		java_files_count = Dir[File.join(dir, '**', '*')].select { |file| File.file?(file) && File.extname(file) == '.java' }.count
		languages << 'Java' if java_files_count > 0
		coffee_script_files = Dir[File.join(dir, '**', '*')].select { |file| File.file?(file) && (File.extname(file) == '.json' || File.extname(file) == '.coffee') }.count
		languages << 'CoffeeScript' if coffee_script_files > 0
		languages

	end 

	def self.clone_project(repo_url,scan)
		@project_title = scan.project_title.present? ? scan.project_title : scan.repo.name
		AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'success',message: "Source Code Scanning Started..." ,scaner_id: scan.id,task_name: @project_title)
		dir = Dir.mktmpdir
		branch = scan.branch_name
		clone = system("git","clone","#{scan.target}",dir) if branch == 'master'
		clone = system("git","clone","-b","#{branch}","#{scan.target}",dir) if branch!= 'master'
		# if private project
		unless clone
			git_details = Setting.find_by(var: scan.source)
			can_clone = git_details.present? && git_details.value.present? && git_details.value['username'].present? && git_details.value['password'].present?
			raise StandardError,"#{scan.source.camelcase} details are not configured" unless can_clone
			git_user = git_details.value['username']
			crypt = ActiveSupport::MessageEncryptor.new(Rails.application.secrets.secret_key_base)
			git_access = crypt.decrypt_and_verify(git_details.value['password'])
			if repo_url.include?("https://")
				git_url = repo_url.split('https://').last
				git_protocol = "https"
			else
				git_url = repo_url.split('http://').last
				git_protocol = "http"
			end
			branch = branch.present? ? branch : 'master'
			system("git","clone","-b","#{branch}","#{git_protocol}://#{git_user}:#{git_access}@#{git_url}",dir)
		end
		return dir
	rescue StandardError => e
		AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: e.message,scaner_id: scan.id,task_name: @project_title)
		scan.update_scan_status("failed",e.message)
		scan.update(last_run: Date.today)
		scan.end_time = Time.now.asctime
		scan.save
		Rails.logger.error "Error inside_gitlab_repo #{e.message}" unless Rails.env == 'test'
		raise e
	end

	def self.is_language_supported(found_langs)
		supported_lang = Setting.pipeline['tasks_for'].keys
		supported = false
		found_langs.each do |lang|
			if supported_lang.include? lang
				supported = true
			end
		end
		supported
	end

	def self.start_tasks(repo_name,source_dir,scan,logfile,scaner_instance)
		pipeline_options = pipeline_settings(repo_name,logfile)
		directories = Array.new
		directories << source_dir
		directories << Dir[File.join(source_dir, '**', '*/')] 
		scan.no_of_processed_files = Dir[File.join(source_dir, '**', '*')].select { |file| File.file?(file)}.count
		scan.save
		directories = directories.flatten.uniq
		pipeline_tools = Setting.find_by(var: 'pipeline')	
		if pipeline_tools.present?
			pipeline_tools = Setting.find_by(var: 'pipeline').value
			pipeline_tools = pipeline_tools['enabled_tools']
		end
		pipeline_tools = Setting.pipeline['enabled_tools'] unless pipeline_tools.present?
		if scan.scan_type == AppConstants::ScanTypes::STATIC
			directories.each do |each_dir| 
				begin  
					dir_langs = code_lang(each_dir)
					is_dir_can_be_process = is_language_supported(dir_langs)
					next unless is_dir_can_be_process
					logfile.puts "log::info processing dir  #{each_dir}.."
					logfile.puts "log::info dir languages....#{dir_langs}"
					pipeline_options[:target] = "#{each_dir}/"
					dir_langs.each do |lang|
						pipeline_options[:run_tasks] << Setting.pipeline['tasks_for'][lang].split(",") unless Setting.pipeline['tasks_for'][lang].nil?
					end
					pipeline_options[:run_tasks] = pipeline_options[:run_tasks].flatten.uniq.compact
					pipeline_options[:run_tasks].delete("Xanitizer")
					if pipeline_tools.present?
						pipeline_options[:run_tasks] = pipeline_options[:run_tasks]  & pipeline_tools.split(",")
					else
						pipeline_options[:run_tasks]  = []
					end
					logfile.puts "log::info selected tools....#{pipeline_options[:run_tasks] }"
					pipeline_options[:run_tasks] << 'Checkmarx' if Rails.env == 'test'
					findings = Array.new
					if pipeline_options[:run_tasks].count > 0
						findings = run_thread(pipeline_options,scan,findings)
					end
					if pipeline_options[:run_tasks].map{|t| t.downcase}.include?('checkmarx')
						checkmarx_options = pipeline_options
						checkmarx_options[:run_tasks] = 'Checkmarx'
						tracker = Pipeline.run(checkmarx_options)
						findings << tracker.findings
					end
					scan.save_findings(findings,scaner_instance) if findings.count > 0
				rescue StandardError => e
					thread_fail_log(scan,logfile,e)
				end
			end
		end
		#running truffle hog scanner
		pipeline_options[:run_tasks].clear
		pipeline_options[:target] = source_dir
		pipeline_options[:run_tasks] = ["TruffleScanner"] if pipeline_tools.present? && pipeline_tools.include?("TruffleScanner")
		begin
			findings = Array.new
			findings = run_thread(pipeline_options,scan,findings)
			scan.save_findings(findings,scaner_instance) if findings.count > 0
			#running xanitizer 
			if scan.found_langs.include?("Java") && scan.scan_type != AppConstants::ScanTypes::HARDCODE
				pipeline_options[:run_tasks].clear
				pipeline_options[:run_tasks] = ["Xanitizer"] if pipeline_tools.present? && pipeline_tools.include?("Xanitizer")
				pipeline_options[:target] = "#{source_dir}"
				@project_cp_dir = pipeline_options[:target]
				findings = Array.new
				findings = run_thread(pipeline_options,scan,findings) if pipeline_options[:run_tasks].count > 0
				scan.save_findings(findings,scaner_instance) if findings.count > 0
			end
		rescue StandardError => e
			thread_fail_log(scan,logfile,e)
		end
	ensure
		logfile.close if logfile && !logfile.closed?
		scan.update_scan_status("Completed","completed on #{current_time}")
		scan.update(last_run: Date.today)
		scan.end_time = Time.now.asctime
		scan.save
		AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'success',message: "Source Code scan completed",scaner_id: scan.id,task_name: @project_title)
		scan.send_notifications
		FileUtils.remove_entry_secure(source_dir)
		FileUtils.remove_entry_secure(@project_cp_dir) if @project_cp_dir.present?
	end

	def self.run_thread(pipeline_options,scan,findings)
		pipeline_thread = Thread.new do
			# run separately and first for checkmarx to make sure it doesn't scan all our downloaded node deps later
			tracker = Pipeline.run(pipeline_options)
			findings << tracker.findings
		end

		[ pipeline_thread].each {|t| t.join}
		findings
	end

	def self.no_support_lang_logs(log_file,scan,langs)
		log_file = File.open(Rails.root.join("log/scans/#{scan.id}.log"), 'a')
		langs = langs.present? ? 'No scanners for' + langs.join(",") : "No scanners for repo languages"
		log_file.puts "[#{self.current_time}] Scan #{scan.id} failed, #{langs} are not supported"
		log_file.close
		$redis.publish "scan:#{scan.id}:log", "[#{self.current_time}] Scan #{scan.id} failed, #{langs} are not supported"
		$redis.publish "scan:#{scan.id}:log", "END_PIPELINE_LOG"
		scan.update_scan_status('failed',"#{langs}")
	end

	def self.thread_fail_log(scan,log_file,e)
		scan.update_scan_status('failed',"error while running tasks #{self.current_time}")
		log_file = File.open(Rails.root.join("log/scans/#{scan.id}.log"), 'a')
		log_file.puts "[#{self.current_time}] error while running tasks"
		log_file.puts  "meassge......#{e.message}"
		log_file.puts "e.backtrace==>#{e.backtrace.inspect}"
		log_file.close
		$redis.publish "scan:#{scan.id}:log", "[#{self.current_time}] error while running tasks"
		$redis.publish "scan:#{scan.id}:log", "END_PIPELINE_LOG"
		Rails.logger.info e.message
		Rails.logger.info e.backtrace
	end
	def self.pipeline_settings(repo_name,logfile)
		{
			:appname => repo_name,
			:quiet => true,
			:npm_registry => Setting.pipeline['npm_registry'],
			:run_tasks => [],
			:pmd_path => Setting.pipeline['pmd_path'],
			:findsecbugs_path => Setting.pipeline['findsecbugs_path'],
			:xanitizer_path=> Setting.pipeline['xanitizer_path'],
			:checkmarx_server => Setting.pipeline['checkmarx_server'],
			:checkmarx_user => Setting.pipeline['checkmarx_user'],
			:checkmarx_password => Setting.pipeline['checkmarx_password'],
			:checkmarx_log => Setting.pipeline['checkmarx_log'],
			:truffle_hog_path => Setting.pipeline['truffle_hog_path'],
			:logfile => logfile
		}

	end

	def self.current_time
		Time.now.to_formatted_s(:short)
	end
end
