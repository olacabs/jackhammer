class GitlabPullInfoWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	sidekiq_options unique: :while_executing
	sidekiq_options({ :queue => :gitpull,:retry => 2 })
	def perform
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		@logfile = File.open(Rails.root.join("log/scans/gitlab_pull.log"), 'a')
		raise StandardError,"Github details are not configured"  unless Setting['gitlab']
		raise StandardError,"Api access token is not configured" unless Setting['gitlab']['api_access_token']
		crypt = ActiveSupport::MessageEncryptor.new(Rails.application.secrets.secret_key_base)

		Gitlab.configure do |config|
			config.endpoint       = Setting['gitlab']['api_end_point'] #ENV['GITLAB_API_ENDPOINT'] # API endpoint URL
			config.private_token  = crypt.decrypt_and_verify(Setting['gitlab']['api_access_token'])    # user's private token
		end
		gitlab = Gitlab.client(endpoint: Setting['gitlab']['api_end_point'],private_token: crypt.decrypt_and_verify(Setting['gitlab']['api_access_token']))
		user = gitlab.user
		begin
			AlertNotification.create(user_id: Setting['gitlab']['user_id'].to_i,identifier: 'task',alert_type: 'success',message: 'Pulling git info started...',task_name: "Git pull")
			Gitlab.groups({all_available:true}).each_page do |groups|
				groups.each do |group|
					begin
						@logfile.puts "group name====#{group.name} "
						group = Gitlab.group(group.id)
						team = Team.find_or_create_by(name: group.name)
						projects = group.projects
						projects.each do |project|
							repo = Repo.where(name: project["name"]).first_or_initialize.tap do |repo|
								repo.ssh_repo_url = project["http_url_to_repo"]
								repo.team = team
								repo.git_type = "gitlab"
								repo.repo_type = "Static"
								repo.save
							end
							branches = Gitlab.branches(project["id"])
							repo_branches = repo.branches.map(&:name)
							branches.each do |branch|
								if repo_branches.include?(branch.name)
									puts "#{branch.name} is existing..."
								else
									branch = Branch.create!(name: branch.name,repo: repo)
									puts "branch name....#{branch.name}"
								end
							end
						end
					rescue Exception=>e
						@logfile.puts "Exception....#{e.inspect}"
					end
				end
			end
		rescue Exception=>e
			@logfile.puts "Exception....#{e.inspect}"
		end
		@logfile.close
		AlertNotification.create(user_id: Setting['gitlab']['user_id'].to_i,identifier: 'task',alert_type: 'success',message: 'Pulling git info done!',task_name: "Git pull")
	rescue StandardError => e               
		@logfile.puts e.message              
		AlertNotification.create(user_id: Setting['gitlab']['user_id'].to_i,identifier: 'task',alert_type: 'danger',message: e.message,task_name: "Git pull")
		@logfile.close 
		raise e 
	end
end
