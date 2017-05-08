class SettingsController < ApplicationController
	def severity
		@tasks = current_user.tasks.map(&:name)
	end
	def scanner
		unauthorized_page unless current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		@list_scans_class = "/settings/scanner"
		@tasks = current_user.tasks.map(&:name)
		@scan_types = ScanType.all
		@static_tools = Task.where(scan_type: 'Static Tools').order(:group).group(:group)

	end
	def update_details
		is_unauthorized = true
		is_unauthorized = false if current_user.is_admin? 
		is_unauthorized = false if Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		if is_unauthorized
			unauthorized_page  
		else
			if params[:settings][:jira].present?
				jira_options = {
					:site => params[:settings][:jira]['host'],
					:username => params[:settings][:jira]['username'],
					:password => params[:settings][:jira]['password'],
					:context_path => '' ,#Setting.jira['context_path'],
					:auth_type => :basic
				}
				jira = JIRA::Client.new(jira_options)
				begin
					jira_project = jira.Project.all.select { |p| p.name.downcase == params[:settings][:jira]["default_project"].downcase }	
					if jira_project.present?
						update_settings
						flash[:notice] = "Jira details are updated"
					else
						flash[:notice] = "Given project is not in jira,Please give right project "
					end
				rescue Exception=>e
					flash[:error] = "Jira is #{e.message}"
				end
				respond_to do |format|
					format.js
				end
			else
				update_settings
				flash[:notice] = "Settings are updated successfully"
				respond_to do |format|
					format.js
				end
			end
		end
	end
	def jira
		unauthorized_page unless current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
	end
	def gitlab
		unauthorized_page unless current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER 
	end
	def github
		unauthorized_page unless current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
	end
	def signup_role
		unauthorized_page unless current_user.is_admin? && Setting.application_mode != AppConstants::UserMode::SINGLE_USER
		@roles = Role.all
	end
	def update
		#unauthorized_page unless current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		if Setting.pipeline['enabled_tools'].present?
			@enabled_tools = Setting.pipeline['enabled_tools'].split(",")
		else
			@enabled_tools = []
		end
		params[:task].each do |key,value|
			task = Task.find_by_name(key)
			if value.to_i == 1
				@enabled_tools << key 
			else
				@enabled_tools.delete(key)
			end
		end
		Setting.merge!(:pipeline, {"enabled_tools"=>@enabled_tools.uniq.join(",")}) if current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		respond_to do |format|
			flash[:notice] = "Tools settings Saved successfully."
			format.js
		end 
	end
	def update_severities
		params[:task].each do |key,value|
			task = Task.find_by_name(key)
			if value.to_i == 1 && !current_user.tasks.include?(task)
				current_user.tasks <<  task
			elsif value.to_i == 0 && current_user.tasks.include?(task)
				current_user.tasks.destroy(task)
			end
		end
		respond_to do |format|
			flash[:notice] = "Severity settings are updated"
			format.js
		end
	end
	def get_mail_info
		@nav_notification_class = '/settings/get_mail_info'
		@notification = current_user.notification
	end
	def update_mail_info
		if current_user.notification.present?
			current_user.notification.update(notification_params)
		else
			notification = Notification.create(notification_params)
			notification.user = current_user
			notification.save
		end
		respond_to do |format|
			flash[:notice] = "e-mails are successfully updated"
			format.html { redirect_to :action=>"get_mail_info"}
		end
	end
	def get_sender_mail
		unauthorized_page unless current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		@nav_notification_class = '/settings/get_sender_mail'
	end
	def pull_corporate_info
		unauthorized_page unless current_user.is_admin?
		respond_to do |format|
			if Setting[params[:pull_repos][:git_type]].present?
				if params[:pull_repos][:git_type] == 'gitlab'
					klass = GitlabPullInfoWorker
				elsif params[:pull_repos][:git_type] == 'github'
					klass = GithubPullInfoWorker
				end
				klass.perform_async
				flash[:notice] = "Pulling git repos info started."
			else
				flash[:alert] = "Api endpoint && Api access token are not configured, Please configure it"
			end
			format.js
		end
	end
	private
	def notification_params
		params.require(:notifications).permit(:critical_count,:high_count,:medium_count,:low_count,:info_count,:critical_email,:high_email,:medium_email,:low_email,:info_email)
	end
end
