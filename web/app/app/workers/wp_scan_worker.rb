require File.join(Rails.root, 'lib', 'wpscan', 'wpscan_helper')
class WpScanWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include Base
	sidekiq_options unique: :while_executing
	sidekiq_options({ :queue => :wordpress,:retry => 2 })
	#
	#MEMCACHED_POOL = ConnectionPool.new(:size => 10, :timeout => 3) { Dalli::Client.new }
	def perform(scan_id,scaner_instance_id)
		scan = Scaner.find(scan_id)
		@scaner_instance = ScanerInstance.find(scaner_instance_id)
		scan.start_time = Time.now.asctime
		scan.save
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		@logfile = File.open(Rails.root.join("log/scans/#{scan.id}.log"), 'a')
		@project_title = scan.project_title.present? ? scan.project_title : scan.repo.name
		AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'success',message: "Wordpress Scanning started..",scaner_id: scan.id,task_name: @project_title)
		scan.update_scan_status("Scanning","Started Scanning on #{Base.current_time}")
		wpscan_options = WpscanOptions.load_from_arguments
		wpscan_options.url = scan.target
		Browser.instance(wpscan_options.to_h.merge(max_threads: wpscan_options.threads))
		wp_target = WpTarget.new(wpscan_options.url, wpscan_options.to_h)
		if wp_target.wordpress_hosted?
			AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: "Wordpress Scanning failed",scaner_id: scan.id,task_name: @project_title)
			@logfile.puts "We do not support scanning *.wordpress.com hosted blogs"
			scan.update_scan_status("failed","We do not support scanning *.wordpress.com hosted blogs")	
		end
		if wp_target.ssl_error?
			AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: "Wordpress Scanning failed",scaner_id: scan.id,task_name: @project_title)
			@logfile.puts "The target site returned an SSL/TLS error"
			scan.update_scan_status("failed","The target site returned an SSL/TLS error")
		end
		unless wp_target.online?
			AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: "Wordpress Scanning failed",scaner_id: scan.id,task_name: @project_title)
			@logfile.puts  "The WordPress URL supplied '#{wp_target.uri}' seems to be down"
			scan.update_scan_status("failed","The WordPress URL supplied '#{wp_target.uri}' seems to be down")
		end
		if wpscan_options.proxy
			proxy_response = Browser.get(wp_target.url)
			unless WpTarget::valid_response_codes.include?(proxy_response.code)
				AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: "Wordpress Scanning failed",scaner_id: scan.id,task_name: @project_title)
				@logfile.puts "Proxy Error :\r\nResponse Code: #{proxy_response.code}\r\nResponse Headers: #{proxy_response.headers}"
				scan.update_scan_status("failed","Proxy Error :\r\nResponse Code: #{proxy_response.code}\r\nResponse Headers: #{proxy_response.headers}")
			end
		end
		if (redirection = wp_target.redirection)
			if redirection =~ /\/wp-admin\/install\.php$/
				@logfile.puts "currently in install mode','Critical','The Website is not fully configured and currently in install mode. Call it to create a new admin user."
				save_finding(scan,'currently in install mode','Critical','The Website is not fully configured and currently in install mode. Call it to create a new admin user.')
			else
				wpscan_options.url = redirection
				wp_target = WpTarget.new(redirection, wpscan_options.to_h)
			end
		end
		unless wpscan_options.force
			unless wp_target.wordpress?
				AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'danger',message: "Wordpress Scanning failed",scaner_id: scan.id,task_name: @project_title)
				@logfile.puts "The remote website is up, but does not seem to be running WordPress."
				scan.update_scan_status("failed","The remote website is up, but does not seem to be running WordPress.")
			end
		end
		start_time   = Time.now
		start_memory = get_memory_usage unless windows?
		if wp_target.has_robots?
			save_finding(scan,"robots.txt","Info","robots.txt available under: '#{wp_target.robots_url}'")
			wp_target.parse_robots_txt.each do |dir|
				save_finding(scan,"robots.txt","Info","Interesting entry from robots.txt: #{dir}")
			end
		end
		if wp_target.has_readme?
			save_finding(scan,"WordPress file exists exposing a version number","High","The WordPress '#{wp_target.readme_url}' file exists exposing a version number")
		end
		if wp_target.has_full_path_disclosure?
			save_finding(scan,"Full Path Disclosure (FPD)","High","Full Path Disclosure (FPD) in '#{wp_target.full_path_disclosure_url}': #{wp_target.full_path_disclosure_data}")
		end
		if wp_target.has_debug_log?
			save_finding(scan,"Debug log file","Critical","Debug log file found: #{wp_target.debug_log_url}")
		end
		wp_target.config_backup.each do |file_url|
			save_finding(scan,"backup file presents","Critical","A wp-config.php backup file has been found in: '#{file_url}'")
		end
		if wp_target.search_replace_db_2_exists?
			save_finding(scan,"searchreplacedb2.php has been found","Critical","searchreplacedb2.php has been found in: '#{wp_target.search_replace_db_2_url}'")
		end
		if wp_target.multisite?
			save_finding(scan,"site seems to be a multisite","Info","This site seems to be a multisite (http://codex.wordpress.org/Glossary#Multisite)")
		end
		if wp_target.has_must_use_plugins?
			save_finding(scan,"Must Use Plugins","Info","This site has 'Must Use Plugins' (http://codex.wordpress.org/Must_Use_Plugins)")
		end

		if wp_target.registration_enabled?
			save_finding(scan,"Registration is enabled","High","Registration is enabled: #{wp_target.registration_url}")
		end

		if wp_target.has_xml_rpc?
			save_finding(scan,"XML-RPC Interface available ","Info","XML-RPC Interface available under: #{wp_target.xml_rpc_url}")
		end

		if wp_target.upload_directory_listing_enabled?
			save_finding(scan,"Upload directory has directory listing enabled","High","Upload directory has directory listing enabled: #{wp_target.upload_dir_url}")
		end

		if wp_target.include_directory_listing_enabled?
			save_finding(scan,"Includes directory has directory listing enabled","High","Includes directory has directory listing enabled: #{wp_target.includes_dir_url}")
		end
		enum_options = {
			show_progression: true,
			exclude_content: wpscan_options.exclude_content_based
		}
		if wp_version = wp_target.version(WP_VERSIONS_FILE)
			findings = wp_version.output(wpscan_options.verbose)
			collect_findings(scan,findings)
		else
			#puts
			puts notice('WordPress version can not be detected')
		end
		if wp_theme = wp_target.theme
			#puts
			# Theme version is handled in #to_s
			puts info("WordPress theme in use: #{wp_theme}")
			findings = wp_theme.output(wpscan_options.verbose)
			collect_findings(scan,findings)

			# Check for parent Themes
			parent_theme_count = 0
			while wp_theme.is_child_theme? && parent_theme_count <= wp_theme.parent_theme_limit
				parent_theme_count += 1

				parent = wp_theme.get_parent_theme
				puts
				puts info("Detected parent theme: #{parent}")
				findings = parent.output(wpscan_options.verbose)
				collect_findings(scan,findings)
				wp_theme = parent
			end

		end
		if wpscan_options.enumerate_plugins == nil and wpscan_options.enumerate_only_vulnerable_plugins == nil
			#puts
			puts info('Enumerating plugins from passive detection ...')

			wp_plugins = WpPlugins.passive_detection(wp_target)
			if !wp_plugins.empty?
				if wp_plugins.size == 1
					puts " | #{wp_plugins.size} plugin found:"
				else
					puts " | #{wp_plugins.size} plugins found:"
				end
				findings = wp_plugins.output(wpscan_options.verbose)
				collect_findings(scan,findings)
			else
				puts info('No plugins found')
			end
		end
		# Enumerate the installed plugins
		if wpscan_options.enumerate_plugins or wpscan_options.enumerate_only_vulnerable_plugins or wpscan_options.enumerate_all_plugins
			puts
			if wpscan_options.enumerate_only_vulnerable_plugins
				puts info('Enumerating installed plugins (only ones with known vulnerabilities) ...')
				plugin_enumeration_type = :vulnerable
			end

			if wpscan_options.enumerate_plugins
				puts info('Enumerating installed plugins (only ones marked as popular) ...')
				plugin_enumeration_type = :popular
			end

			if wpscan_options.enumerate_all_plugins
				puts info('Enumerating all plugins (may take a while and use a lot of system resources) ...')
				plugin_enumeration_type = :all
			end
			puts

			wp_plugins = WpPlugins.aggressive_detection(wp_target,
								    enum_options.merge(
									    file: PLUGINS_FILE,
									    type: plugin_enumeration_type
			)
								   )

			puts
			if !wp_plugins.empty?
				puts info("We found #{wp_plugins.size} plugins:")
				findings = wp_plugins.output(wpscan_options.verbose)
				collect_findings(scan,findings)
			else
				puts info('No plugins found')
			end
		end
		# Enumerate installed themes
		if wpscan_options.enumerate_themes or wpscan_options.enumerate_only_vulnerable_themes or wpscan_options.enumerate_all_themes
			puts
			if wpscan_options.enumerate_only_vulnerable_themes
				puts info('Enumerating installed themes (only ones with known vulnerabilities) ...')
				theme_enumeration_type = :vulnerable
			end

			if wpscan_options.enumerate_themes
				puts info('Enumerating installed themes (only ones marked as popular) ...')
				theme_enumeration_type = :popular
			end

			if wpscan_options.enumerate_all_themes
				puts info('Enumerating all themes (may take a while and use a lot of system resources) ...')
				theme_enumeration_type = :all
			end
			puts

			wp_themes = WpThemes.aggressive_detection(wp_target,
								  enum_options.merge(
									  file: THEMES_FILE,
									  type: theme_enumeration_type
			)
								 )
			puts
			if !wp_themes.empty?
				puts info("We found #{wp_themes.size} themes:")
				findings = wp_themes.output(wpscan_options.verbose)
				collect_findings(scan,findings)
			else
				puts info('No themes found')
			end
		end
		if wpscan_options.enumerate_timthumbs
			puts
			puts info('Enumerating timthumb files ...')
			puts

			wp_timthumbs = WpTimthumbs.aggressive_detection(wp_target,
									enum_options.merge(
										file: DATA_DIR + '/timthumbs.txt',
										theme_name: wp_theme ? wp_theme.name : nil
			)
								       )
			puts
			if !wp_timthumbs.empty?
				puts info("We found #{wp_timthumbs.size} timthumb file/s:")
				findings = wp_timthumbs.output(wpscan_options.verbose)
				collect_findings(scan,findings)
			else
				puts info('No timthumb files found')
			end
		end

		# If we haven't been supplied a username/usernames list, enumerate them...
		if !wpscan_options.username && !wpscan_options.usernames && wpscan_options.wordlist || wpscan_options.enumerate_usernames
			puts
			puts info('Enumerating usernames ...')

			if wp_target.has_plugin?('stop-user-enumeration')
				puts warning("Stop User Enumeration plugin detected, results might be empty. However a bypass exists for v1.2.8 and below, see stop_user_enumeration_bypass.rb in #{__dir__}")
			end

			wp_users = WpUsers.aggressive_detection(wp_target,
								enum_options.merge(
									range: wpscan_options.enumerate_usernames_range,
									show_progression: false
			)
							       )

			if wp_users.empty?
				puts info('We did not enumerate any usernames')

				if wpscan_options.wordlist
					puts 'Try supplying your own username with the --username option'
					puts
					exit(1)
				end
			else
				puts info("Identified the following #{wp_users.size} user/s:")
				wp_users.output(margin_left: ' ' * 4)
				if wp_users[0].login == "admin"
					puts warning("Default first WordPress username 'admin' is still used")
				end
			end

		else
			wp_users = WpUsers.new

			if wpscan_options.usernames
				File.open(wpscan_options.usernames).each do |username|
					wp_users << WpUser.new(wp_target.uri, login: username.chomp)
				end
			else
				wp_users << WpUser.new(wp_target.uri, login: wpscan_options.username)
			end
		end
		# Start the brute forcer
		bruteforce = true
		if wpscan_options.wordlist
			if wp_target.has_login_protection?

				protection_plugin = wp_target.login_protection_plugin()

				puts
				puts warning("The plugin #{protection_plugin.name} has been detected. It might record the IP and timestamp of every failed login and/or prevent brute forcing altogether. Not a good idea for brute forcing!")
				puts '[?] Do you want to start the brute force anyway ? [Y]es [N]o, default: [N]'

				bruteforce = false if wpscan_options.batch || Readline.readline !~ /^y/i
			end

			if bruteforce
				puts info('Starting the password brute forcer')

				begin
					wp_users.brute_force(
						wpscan_options.wordlist,
						show_progression: true,
						verbose: wpscan_options.verbose
					)
				ensure
					puts
					wp_users.output(show_password: true, margin_left: ' ' * 2)
				end
			else
				puts critical('Brute forcing aborted')
			end
		end

		stop_time   = Time.now
		elapsed     = stop_time - start_time
		used_memory = get_memory_usage - start_memory unless windows?
		puts
		puts info("Finished: #{stop_time.asctime}")
		puts info("Requests Done: #{@total_requests_done}")
		puts info("Memory used: #{used_memory.bytes_to_human}") unless windows?
		puts info("Elapsed time: #{Time.at(elapsed).utc.strftime('%H:%M:%S')}")
	rescue Exception=>e 
		@logfile = File.open(Rails.root.join("log/scans/#{scan.id}.log"), 'a')
		@logfile.puts "Error===>#{e.inspect}"
		@logfile.close

	ensure
		scan = Scaner.find(scan_id)
		sev_hash = {"Critical"=>0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0}
		findings = scan.findings.group_by_severity
		findings.each do |sev_counts|
			sev_hash[sev_counts[0]] = sev_counts[1]
		end
		@total_count = sev_hash["Critical"] + sev_hash["High"] + sev_hash["Medium"] + sev_hash["Low"] + sev_hash["Info"]
		scan.update(critical_count: sev_hash["Critical"] ,high_count: sev_hash["High"] ,medium_count: sev_hash["Medium"] ,low_count: sev_hash["Low"],info_count: sev_hash["Info"],total_count: @total_count,status: 'Completed' ,status_reason: 'Wp Scan completed')
		scan.update_scan_status("Completed","completed") unless scan.status == 'failed'
		scan.end_time = Time.now.asctime
		scan.save
		AlertNotification.create(user_id: scan.user_id,identifier: 'task',alert_type: 'success',message: "Wordpress scan completed",task_name: @project_title,scaner_id: scan.id)
		scan.send_notifications
	end
	def save_finding(scan,description,severity,detail,references=nil,solution=nil)
		bug_solution = solution.present? ? "Fixed in #{solution}" : ""
		cvss_score = references.present? ? references["cve"] : ""
		external_link = references.present? ? references["url"].join(",") : ""
		fingerprint = Digest::MD5.hexdigest("#{scan.target}#{description}#{severity}#{detail}#{external_link}#{bug_solution}")
		if scan.team_id.present?
			previous = Finding.by_repo_id(scan.repo_id).by_teams(scan.team_id).fingerprint(fingerprint).order("created_at").last
		else
			previous = Finding.by_repo_id(scan.repo_id).by_user(scan.user_id).fingerprint(fingerprint).order("created_at").last
		end
		if previous.present?
			unless scan.findings.include?(previous)
				scan.findings << previous
				@scaner_instance.findings << previous
				#scan.increment_scanner_sev_count(scan,scaner_instance,finding)
			end
			previous.scaners << scan unless previous.scaners.include?(scan)
		else
			find = Finding.create(description: description,severity: severity,detail: detail,external_link: external_link,solution: bug_solution,scaner_instance_id: @scaner_instance.id,repo_id: scan.repo.id,cvss_score: cvss_score,scanner: 'Wpscan',owner_type: scan.owner_type,team_id: scan.team_id,scan_type: scan.scan_type,fingerprint: fingerprint,user_id: scan.user_id,scaner_id: scan.id)

			if find.valid?
				#scan.increment_sev_count(find,scan,@scanner_instance)
				find.scaners << scan unless find.scaners.include?(scan)
				scan.findings << find unless scan.findings.include?(find)
			end
		end
	end
	def collect_findings(scan,findings)
		if findings.present? && findings.first.class.to_s == "Vulnerability"
			findings.each do |find|
				save_finding(scan,find.title,'High',find.title,find.references,find.fixed_in)
			end
		end
	end
end
