class ScanersController < ApplicationController
	include ApplicationHelper
	#	load_and_authorize_resource
	#	skip_authorize_resource :only => [:add_dynamic_scans,:new,:add_target,:load_branches,:run]
	before_action :setup_scan,only: [:run,:scan_wise_results,:list_scan_results,:unschedule]
	def run
		start_scan
		respond_to do |format|
			flash[:notice]="Scanning Started......."
			format.js
		end
	rescue => e 
		respond_to do |format|
			@msg= e.message
			format.js
		end
	end

	def new
		get_roles
		unauthorized_page unless is_scan_create_enabled?
		@active_scan_class = '/scaners/new'
		@user_repos = current_user.repos
		is_static_scan = params[:scan_type] == AppConstants::ScanTypes::STATIC ||  params[:scan_type] == AppConstants::ScanTypes::HARDCODE 
		if current_user.is_team_lead? && params[:owner_type] == AppConstants::OwnerTypes::TEAM && is_static_scan
			if current_user.teams.present?
				@user_repos = current_user.teams.map { |t| t.repos}.flatten
			else
				@user_repos = []
			end
			if  !@user_repos.present?
				flash[:error] = "You can not do scan , since You are not belongs to any team or no repos for the team"
				redirect_to :action=> "add_target" 
			end
		end
	end

	def index
		@active_nav_class = '/scaners'
		get_roles
		if (!current_user.is_admin? && !current_user.is_security_member? && params[:owner_type] == AppConstants::OwnerTypes::CORPORATE) || is_scan_read_enabled? == false
			unauthorized_page 
		else
			session[:owner_type] = params[:owner_type]
			session[:scan_type] = params[:scan_type]
			session[:is_scheduled] = params[:is_scheduled]
			@new_target_url = session[:scan_type] == AppConstants::ScanTypes::STATIC ? new_scaner_path  : add_dynamic_scans_scaners_path(owner_type: session[:owner_type],scan_type: params[:scan_type])
			current_user_roles
			@is_scan_read_present = params[:scan_type].present? ? is_scan_read_enabled? : true
			@is_scan_create_present = params[:scan_type].present? ? is_scan_create_enabled? : true
			@is_scan_delete_present = params[:scan_type].present? ? is_scan_delete_enabled? : true
			@is_scan_update_present = params[:scan_type].present? ? is_scan_update_enabled? : true
			respond_to do |format|
				format.html
				format.json { render json: ListScans.new(view_context,@is_scan_read_present,@is_scan_create_present,@is_scan_delete_present,current_user) }
			end
		end
	end
	def show

	end

	def create
		commit_type = params[:commit]
		if scaner_params[:scan_type] == AppConstants::ScanTypes::MOBILE && scaner_params[:periodic_schedule].present?
			respond_to do |format|
				format.html { render 'new' }
			end
		else
			if scaner_params[:repo_id].present?
				repo = Repo.find(scaner_params[:repo_id])
				@scan = current_user.scaners.build(scaner_params)
				@scan.team = repo.team
				params[:scaner][:owner_type] = AppConstants::OwnerTypes::TEAM
			else
				@scan = current_user.scaners.build(scaner_params)
				target = scaner_params[:target]
				target = scaner_params[:project_target].original_filename if scaner_params[:scan_type] == AppConstants::ScanTypes::MOBILE
				repo = Repo.find_by_ssh_repo_url(target)
				repo_type = scaner_params[:scan_type]
				#	repo = Repo.create(ssh_repo_url: target,name: scaner_params[:project_title],repo_type: repo_type) unless repo.present?
				repo = Repo.where(ssh_repo_url: target).first_or_initialize.tap do |repo|
					repo.ssh_repo_url = target
					repo.name = scaner_params[:project_title]
					repo.git_type = scaner_params[:source]
					repo.repo_type = repo_type
					repo.save
				end
				repo.update(team_id: scaner_params[:team_id]) if scaner_params[:team_id].present?
				current_user.repos <<  repo if scaner_params[:owner_type] == AppConstants::OwnerTypes::PERSONAL && !current_user.repos.include?(repo)
				if @scan.scan_type == AppConstants::ScanTypes::STATIC || @scan.scan_type == AppConstants::ScanTypes::HARDCODE
					repo.update(repo_type: AppConstants::ScanTypes::STATIC)
					branch = Branch.find_or_create_by(:repo_id => repo.id, :name => scaner_params[:branch_name]) 
				end
				@scan.repo = repo
			end
			@scan.status = AppConstants::ScanStatus::PENDING
			@scan.password_param_val = AESCrypt.encrypt(scaner_params[:password_param_val],Rails.application.secrets.secret_key_base) if scaner_params[:password_param_val].present?
			respond_to do |format|
				if @scan.save
					logger.info "Scan instance id #{@scan.id} added by user #{current_user.id}"
					flash[:notice] = "Scan Target added successfully"
					@scan.update(owner_type: AppConstants::OwnerTypes::CORPORATE) if params[:scaner][:owner_type] == AppConstants::OwnerTypes::TEAM
					if commit_type == "Scan Now"	
						@scan.update(status: 'Queued')
						start_scan
						flash[:notice] = "Scanning started.."
					end
					format.html { redirect_to :action=>"index",scan_type: scaner_params[:scan_type],owner_type: params[:scaner][:owner_type]}
				else
					flash[:error] = @scan.errors.full_messages.first
					if params[:scan_type] == AppConstants::ScanTypes::STATIC || @scan.scan_type == AppConstants::ScanTypes::HARDCODE
						format.html { redirect_to :action=>'new', scan_type: scaner_params[:scan_type],owner_type: params[:scaner][:owner_type] } 
					else
						format.html { redirect_to :action=>'add_dynamic_scans',scan_type: scaner_params[:scan_type],owner_type: params[:scaner][:owner_type] } 
					end
				end
			end
		end
	end
	def store_bulk_scans
		params[:scaners][:repo_id].each do |repo_id|
			if repo_id.present?
				repo = Repo.find(repo_id)
				@scan = Scaner.create(repo_id: repo_id,team_id: repo.team.id,scan_type: params[:scaners][:scan_type],source: repo.git_type,status: AppConstants::ScanStatus::QUEUED,user_id: current_user.id,periodic_schedule: params[:scaners][:periodic_schedule],owner_type: AppConstants::OwnerTypes::CORPORATE)
				#start_scan if repo.git_type.present?
			end
		end
		StartScanWorker.perform_async
		respond_to do |format|  
			flash[:notice] = "Scan Target added successfully"
			format.html { redirect_to :action=>"index",scan_type: params[:scaners][:scan_type],owner_type: AppConstants::OwnerTypes::CORPORATE}
		end
	end
	def destroy
		@scan = Scaner.find(params[:id])
		if (@scan.owner_type == AppConstants::OwnerTypes::PERSONAL && @scan.user == current_user) || (@scan.owner_type == AppConstants::OwnerTypes::CORPORATE && (current_user.is_admin? || current_user.is_security_member?)) || (@scan.owner_type == AppConstants::OwnerTypes::CORPORATE && @scan.team == current_user.team)
			@scan.scaner_instances.destroy_all if @scan.scaner_instances.present?
			@scan.findings.destroy_all if @scan.findings.present? && @scan.findings.count > 0
			@scan.repo.findings.destroy_all if @scan.repo.present? && @scan.repo.findings.present? && @scan.repo.findings.count > 0
			@scan.destroy
			flash[:notice] = "Scan target deleted successfully"
		else
			flash[:error] = "Scan does not belongs to current team/user"
		end
		respond_to do |format|
			format.js
		end             
	rescue => e             
		respond_to do |format|
			@msg= e.message
			format.js
		end
	end

	def load_projects
		teams = Team.where(id: params[:team_id].split(","))
		count = 0
		teams.each  { |team|
			if count!=0
				@repos = @repos | team.repos
			else
				@repos = team.repos
			end
			count = count  + 1
		}
		respond_to do |format|
			format.json { render json: @repos }
		end
	end

	def load_branches
		repo = Repo.find(params[:repo_id])
		branches = repo.branches
		respond_to do |format|
			format.json { render json: branches }
		end
	end
	def list_scan_results
		@month_hash = get_last_one_years_moths
		@findings = @scan.findings.not_false_positive.by_tools(@severity_levels).by_sev(@scaners_levels)
		month_wise_findings(@findings)
		@severity_hash = fetch_findings_severity_wise(@findings)
		copy_prev_month_stats
		get_findings_array
	end
	def scan_wise_results
		@scan_wise_results_active_path = "/scaners/scan_wise_results"
		unauthorized_page unless current_user.is_admin? || current_user.is_security_member?
		@findings = @scan.findings.not_false_positive.by_tools(@severity_levels).by_sev(@scaners_levels)
	end
	def start_scan_for_all_repo
		StartScanWorker.perform_async
		respond_to do |format|
			flash[:notice] = "Started scanning for each repo"
			format.js
		end
	end
	def add_dynamic_scans
		get_roles
		unauthorized_page unless is_scan_create_enabled?
		@active_nav_class = "/scaners/add_dynamic_scans"
		if current_user.is_admin? || current_user.is_security_member?
			@user_teams =  Team.where.not(name: ['Network','Mobile','Wordpress']) if params[:scan_type] == AppConstants::ScanTypes::WEB
			@user_teams =  Team.where.not(name: ['Network','Web','Wordpress']) if params[:scan_type] == AppConstants::ScanTypes::MOBILE
			@user_teams =  Team.where.not(name: ['Network','Web','Mobile']) if params[:scan_type] == AppConstants::ScanTypes::WORDPRESS
			@user_teams = Team.where.not(name: ['Mobile','Web','Wordpress']) if  params[:scan_type] == AppConstants::ScanTypes::NETWORK
		elsif current_user.is_team_lead?
			@user_teams = current_user.teams | Team.where(name: params[:scan_type])
			@user_repos = @user_teams.map { |t| t.repos}.flatten
			is_static_scan = params[:scan_type] == AppConstants::ScanTypes::STATIC ||  params[:scan_type] == AppConstants::ScanTypes::HARDCODE
			if current_user.is_team_lead? && params[:owner_type] ==  AppConstants::OwnerTypes::TEAM && !@user_repos.present? && is_static_scan
				flash[:error] = "You can not do scan , since You are not belongs to any team or no repos for the team"
				redirect_to :action=> "add_target"
			end
			#@user_teams = @user_teams | current_user.teams
		end
	end
	def scans_diff
		scan_ids = params[:compare_scans][:scan_ids].split(",")
		@first_scan = Scaner.find(scan_ids.first)
		@second_scan = Scaner.find(scan_ids.last)
		severity_levels.each { |sev| 	instance_variable_set "@first_scan_#{sev.downcase}".to_sym, @first_scan.findings.by_sev(sev).not_false_positive.count}
		severity_levels.each { |sev|    instance_variable_set "@second_scan_#{sev.downcase}".to_sym, @second_scan.findings.by_sev(sev).not_false_positive.count}
		@first_array = [@first_scan_critical,@first_scan_high,@first_scan_medium,@first_scan_low,@first_scan_info]
		@second_array = [@second_scan_critical,@second_scan_high,@second_scan_medium,@second_scan_low,@second_scan_info]	
	end
	def download_results
		@scan = Scaner.find(params[:scan_id])
		@findings = @scan.findings
		@file_title = @scan.project_title.present? ? @scan.project_title : @scan.repo.name
		respond_to do |format|
			format.html
			format.csv { send_data generate_csv}
			format.xls 
		end
	end
	def change_find_status 
		findings = params["marked"]["findings"].split(",")
		Finding.update_finding(findings,current_user,params[:commit])
		if params[:marked][:table_id]!='filter_findings_table'
			first_find = Finding.find(findings.first)
			sev_hsh = Hash.new
			get_user_settings
			if first_find.team_id.present?
				not_flase_severity_counts = Finding.by_corporate.by_repo_id(first_find.repo_id).not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(first_find.scan_type).group_by_severity
				flase_severity_counts = Finding.by_corporate.by_repo_id(first_find.repo_id).by_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(first_find.scan_type).group_by_severity if current_user.is_security_member? || current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
			else
				not_flase_severity_counts = Finding.by_not_corporate.by_repo_id(first_find.repo_id).not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(first_find.scan_type).by_user(current_user.id).group_by_severity
				flase_severity_counts = Finding.by_not_corporate.by_repo_id(first_find.repo_id).by_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).by_scan_type(first_find.scan_type).by_user(current_user.id).group_by_severity if current_user.is_security_member? || current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
			end
			@not_false_sev_hash = {"Critical"=>0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0}
			not_flase_severity_counts.each { |sev| @not_false_sev_hash[sev[0]] = sev[1]}
			if current_user.is_security_member? ||  current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
				@false_sev_hash = {"Critical"=>0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0}
				flase_severity_counts.each { |sev| @false_sev_hash[sev[0]] = sev[1]}
			end
		end
		if params[:commit].include?("Valid")
			flash[:notice] = "Selected findings are marked as valid"
		else
			flash[:notice] = params[:commit].include?("Exploitable") ? "Selected findings are marked as  not Exploitable" : "Selected findings are marked as false positive"
		end
		respond_to do |format|
			format.js
		end     
	end         
	def add_target

	end
	def unschedule
		@scan.update(periodic_schedule: "")		
		respond_to do |format|
			flash[:notice] = "Unscheduled is done for selected scan"
			format.js
		end
	end
	private
	def start_scan
		logger.info "Starting worker for #{@scan.id}"
		scaner_instance = @scan.scaner_instances.build
		scaner_instance.save
		if @scan.source != 'gitlab' && @scan.source != 'github'
			klass = (@scan.source.camelize + 'ScanWorker').constantize
			klass.perform_async(@scan.id,scaner_instance.id)
		else
			GitScanWorker.perform_async(@scan.id,scaner_instance.id)
		end
	end
	def scaner_params
		params.require(:scaner).permit(:project_title,:target,:branch_name,:source,:project_target,:team_id,:repo_id,:branch_id,:periodic_schedule,:scan_type,:parameters,:owner_type,:username_param,:password_param,:username_param_val,:password_param_val,:web_login_url)
	end
	def is_scan_read_enabled?
		is_scan_enabled? || (@functionalities.present? && @functionalities.include?("Scan") &&  @operations_list.include?("scan_read"))
	end
	def is_scan_create_enabled?
		is_scan_enabled? || (@functionalities.present? && @functionalities.include?("Scan") && @operations_list.include?("scan_create"))
	end
	def is_scan_delete_enabled?
		is_scan_enabled? || (@functionalities.present? && @functionalities.include?("Scan") &&  @operations_list.include?("scan_delete"))
	end
	def is_scan_update_enabled?
		is_scan_enabled? || (@functionalities.present? && @functionalities.include?("Scan") && @operations_list.include?("scan_update"))
	end
	def is_scan_enabled?
		current_user.is_admin? || current_user.is_team_lead? || current_user.is_security_member? ||  Setting.application_mode == AppConstants::UserMode::SINGLE_USER
	end
	def setup_scan
		@scan = Scaner.find(params[:scan_id])
		get_user_settings
	end
end
