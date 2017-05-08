class ReposController < ApplicationController
	before_action :setup_repo_and_settings,except: [:index,:get_team_repos,:list_apps]
	def index
		@teams =  Team.where.not(name: ['Network','Mobile','Wordpress','Web'])
		@teams =  Team.where.not(name: ['Network','Mobile','Wordpress']) if params[:scan_type] == AppConstants::ScanTypes::WEB
		@teams =  Team.where.not(name: ['Network','Web','Wordpress']) if params[:scan_type] == AppConstants::ScanTypes::MOBILE
		@teams =  Team.where.not(name: ['Network','Web','Mobile']) if params[:scan_type] == AppConstants::ScanTypes::WORDPRESS
		@teams = Team.where.not(name: ['Mobile','Web','Wordpress']) if  params[:scan_type] == AppConstants::ScanTypes::NETWORK
		get_repos_sev_count
	end
	def list_apps
		@applications_path = '/repos/list_apps'
		get_repos_sev_count
		#       respond_to do |format|
		#               format.html
		#               format.json { render json: ListApps.new(view_context,current_user) }
		#       end  
	end

	def repo_summary
		get_findings
		session[:page] = params[:page]
		@month_hash = get_last_one_years_moths
		month_wise_findings(@findings)
		@severity_hash = fetch_findings_severity_wise(@findings)
		@false_severity_hash = fetch_findings_severity_wise(@false_findings)
		copy_prev_month_stats
		get_findings_array
	end
	def repo_tool_wise_findings
		@repo_tools_results_active_path = "/repos/repo_tool_wise_findings"
		unauthorized_page unless current_user.is_admin? || current_user.is_security_member? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		get_findings
		@severity_hash = fetch_findings_severity_wise(@findings)
		@false_severity_hash = fetch_findings_severity_wise(@false_findings)
		get_findings
	end
	def repo_severity_wise_findings
		@applications_path = '/teams/repo_severity_wise_findings'
		get_roles
		@can_mark_flase_positive = can_change?
		get_findings
		@severity_hash = fetch_findings_severity_wise(@findings)
		@false_severity_hash = fetch_findings_severity_wise(@false_findings)
		@sev_active_class_not_false = ""
		@sev_active_class_with_false = ""
		if params[:false_finds].present?
			@sev_active_class_with_false = "active" 
		else
			@sev_active_class_not_false = "active"
		end
		respond_to do |format|
			format.html
			format.json { render json: SeverityFindings.new(view_context,@repo,params[:severity],current_user)}
		end
	end
	def repo_channel_results
		unauthorized_page unless current_user.is_admin? || current_user.is_security_member? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		get_findings
		@severity_hash = fetch_findings_severity_wise(@findings)
		@false_severity_hash = fetch_findings_severity_wise(@false_findings)
		@repo_tools_results_active_path = '/repos/repo_channel_results'
		respond_to do |format|
			format.html
			format.json { render json: ChannelFindings.new(view_context,@repo,params[:channel],params[:date]) }
		end
	end
	def delete_repo_channel_results
		unauthorized_page unless current_user.is_admin? || current_user.is_security_member? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		respond_to do |format|
			findings = Finding.where("scanner = ? and date(created_at) = ? and repo_id= ?" , params[:channel],params[:date].to_date,@repo.id)
			findings.each do |find|
				Scaner.update_sev_count(find)
			end
			findings.destroy_all
			format.html { redirect_to :action=>"repo_tool_wise_findings",:repo_id=>params["repo_id"],:owner_type=>params[:owner_type],:scan_type=>params[:scan_type]}
		end
	end
	def get_findings
		scan_types = params[:scan_type] == AppConstants::ScanTypes::STATIC || params[:scan_type] == AppConstants::ScanTypes::HARDCODE ? [AppConstants::ScanTypes::STATIC,AppConstants::ScanTypes::HARDCODE] : params[:scan_type]
		if params[:owner_type] == AppConstants::OwnerTypes::CORPORATE || params[:owner_type] == AppConstants::OwnerTypes::TEAM
			@false_findings = Finding.by_corporate.by_repo_id(params[:repo_id]).by_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(scan_types)
			@findings = Finding.by_corporate.by_repo_id(params[:repo_id]).not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(scan_types)
		else
			@false_findings = Finding.by_not_corporate.by_repo_id(params[:repo_id]).by_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(scan_types).by_user(current_user.id)
			@findings = Finding.by_not_corporate.by_repo_id(params[:repo_id]).not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(scan_types).by_user(current_user.id)
		end
		@findings = @findings.uniq
		@false_findings = @false_findings.uniq
	end
	def  get_team_repos
		get_repos_sev_count
		respond_to do |format|
			format.js
		end
	end
	def get_repos_sev_count
		@team = Team.find_by_name(params[:team_name])
		session[:team_name] = params[:team_name]
		get_user_settings
		scan_types = params[:scan_type] == AppConstants::ScanTypes::STATIC || params[:scan_type] == AppConstants::ScanTypes::HARDCODE ? [AppConstants::ScanTypes::STATIC,AppConstants::ScanTypes::HARDCODE] : params[:scan_type]
		if params[:owner_type] == AppConstants::OwnerTypes::CORPORATE || params[:owner_type] == AppConstants::OwnerTypes::TEAM	
			repos_findings = Finding.by_team_id(@team.id).not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(scan_types).group_by_repo_sev_count
		else
			repos_findings = Finding.by_not_corporate.not_false_positive.by_user(current_user.id).by_scan_type(scan_types).by_tools(@scaners_levels).by_sev(@severity_levels).group_by_repo_sev_count			
		end
		repo_find_hash = Hash.new
		repos_findings.each do |repo_find|
			repo_find_hash[repo_find[0]] = {"Critical" => 0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0} unless repo_find_hash[repo_find[0]].present?
			repo_find_hash[repo_find[0]][repo_find[1]] = repo_find[2]
		end
		if params[:owner_type] == AppConstants::OwnerTypes::CORPORATE
			@projects = @team.repos.corpo_repos
			@projects = @team.repos.by_repo_type(params[:scan_type]) if params[:scan_type]!=AppConstants::ScanTypes::STATIC
		else
			@projects  = current_user.repos.by_repo_type(scan_types)	
		end
		@repo_sev_hash = Hash.new
		@repo_names_with_sev_hash = Hash.new
		@projects.each do |each_project|
			if  repo_find_hash.include?(each_project.id)
				total_count = repo_find_hash[each_project.id]["Critical"] + repo_find_hash[each_project.id]["High"] + repo_find_hash[each_project.id]["Medium"] + repo_find_hash[each_project.id]["Low"] + repo_find_hash[each_project.id]["Info"]
				@repo_sev_hash[each_project.id] = {"Total"=> total_count,"Critical" => repo_find_hash[each_project.id]["Critical"],"High"=>repo_find_hash[each_project.id]["High"],"Medium"=>repo_find_hash[each_project.id]["Medium"],"Low"=>repo_find_hash[each_project.id]["Low"],"Info"=>repo_find_hash[each_project.id]["Info"]}
			else
				@repo_sev_hash[each_project.id] =  {"Total"=>0,"Critical" => 0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0}
			end
			if params[:scan_type] == AppConstants::ScanTypes::STATIC || params[:scan_type] == AppConstants::ScanTypes::HARDCODE
				@repo_names_with_sev_hash[each_project.name + "," + each_project.id.to_s] = @repo_sev_hash[each_project.id]
			else
				@repo_names_with_sev_hash[each_project.ssh_repo_url  + "," + each_project.id.to_s] = @repo_sev_hash[each_project.id] if each_project.ssh_repo_url.present?
			end
		end
	end
	private
	def setup_repo_and_settings
		@repo = Repo.find(params[:repo_id])
		get_user_settings
	end
	def can_change?
		current_user.is_admin? || current_user.is_security_member? || current_user.is_team_lead? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER ||  (@functionalities.present? && @functionalities.include?("Mark False Positive"))
	end
end
