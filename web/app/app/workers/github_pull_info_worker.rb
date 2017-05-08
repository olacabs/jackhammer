class GithubPullInfoWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	sidekiq_options unique: :while_executing
	sidekiq_options({ :queue => :gitpull,:retry => 2 })
	def perform
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		@logfile = File.open(Rails.root.join("log/scans/github_pull.log"), 'a')
		raise StandardError,"Github details are not configured" unless Setting['github']
		raise StandardError,"Api access token is not configured" unless Setting['github']['api_access_token']
		crypt = ActiveSupport::MessageEncryptor.new(Rails.application.secrets.secret_key_base)
		client = Octokit::Client.new(:access_token => crypt.decrypt_and_verify(Setting['github']['api_access_token']))
		organization = client.orgs.first[:login]
		git_teams = client.organization_teams(organization)
		Dir.mkdir(Rails.root + "log/scans") unless File.exists?(Rails.root + "log/scans")
		AlertNotification.create(user_id: Setting['gitlab']['user_id'].to_i,identifier: 'task',alert_type: 'success',message: 'Pulling git info started...',task_name: "Git pull")
		git_teams.each do |git_team|
			begin
				team = Team.find_or_create_by(name: git_team[:name])
				@logfile.puts "pulling teams info of #{git_team[:name]}..and team id#{git_team[:id]}"
				team_repos = client.team_repositories(git_team[:id])
				team_repos.each do |project| 
					repo = Repo.where(name: project[:name]).first_or_initialize.tap do |repo|
						repo.ssh_repo_url = project[:clone_url]
						repo.team = team
						repo.git_type = "github"
						repo.repo_type = "Static"
						repo.save
					end
					branches = client.branches(project[:owner][:id])
					repo_branches = repo.branches.map(&:name)
					branches.each do |branch|
						if repo_branches.include?(branch.name)
							puts "#{branch.name} is existing..."
						elsif branch[:name]!= 'gh-pages' && if branch[:name]!= 'v128'
							branch = Branch.create!(name: branch.name,repo: repo) 
							puts "branch name....#{branch.name}"
						else

						end
					end
				end
			end
		rescue Exception=>e
			@logfile.puts "Some exception is occured...#{e.inspect}"
		end
		AlertNotification.create(user_id: Setting['gitlab']['user_id'].to_i,identifier: 'task',alert_type: 'success',message: 'Pulling git info done!',task_name: "Git pull")
	  end
		@logfile.close
	rescue StandardError => e
	@logfile.puts e.message
	AlertNotification.create(user_id: Setting['github']['user_id'].to_i,identifier: 'task',alert_type: 'danger',message: e.message,task_name: "Git pull")
	@logfile.close
	raise e
    end
end
