class TeamsController < ApplicationController
	load_and_authorize_resource
	skip_authorize_resource :only => [:fetch_projects,:list_corpo_groups,:index]
	before_action :set_group, only: [:edit, :update, :destroy]
	def index
		#	@teams = Team.order(:name)	
		session[:team_name] = params[:name]
		fetch_projects
		#respond_to do |format|
		#	format.html
		#	format.json { render json: TeamsDatatable.new(view_context) }
		#end

	end
	def list_corpo_groups
		@applications_path = '/teams/list_corpo_groups'
		get_user_settings
		scan_types = params[:scan_type] == AppConstants::ScanTypes::STATIC ? [AppConstants::ScanTypes::STATIC,AppConstants::ScanTypes::HARDCODE] : params[:scan_type]
		corp_findings = Finding.by_corporate.not_false_positive.by_scan_type(scan_types).by_tools(@scaners_levels).by_sev(@severity_levels).group_by_team_sev_count
		corpo_find_hash = Hash.new
		corp_findings.each do |corpo_find|
			corpo_find_hash[corpo_find[0]] = {"Critical" => 0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0} unless corpo_find_hash[corpo_find[0]].present?
			corpo_find_hash[corpo_find[0]][corpo_find[1]] = corpo_find[2]
		end
		teams =  Team.where.not(name: ['Network','Mobile','Wordpress','Web'])
		teams =  Team.where.not(name: ['Network','Mobile','Wordpress']) if params[:scan_type] == AppConstants::ScanTypes::WEB
                teams =  Team.where.not(name: ['Network','Web','Wordpress']) if params[:scan_type] == AppConstants::ScanTypes::MOBILE
                teams =  Team.where.not(name: ['Network','Web','Mobile']) if params[:scan_type] == AppConstants::ScanTypes::WORDPRESS
                teams = Team.where.not(name: ['Mobile','Web','Wordpress']) if  params[:scan_type] == AppConstants::ScanTypes::NETWORK
		@team_sev_hash = Hash.new
		@team_names_with_sev_hash = Hash.new
		teams.each do |each_team|
			if  corpo_find_hash.include?(each_team.id)
				total_count = corpo_find_hash[each_team.id]["Critical"] + corpo_find_hash[each_team.id]["High"] + corpo_find_hash[each_team.id]["Medium"] + corpo_find_hash[each_team.id]["Low"] + corpo_find_hash[each_team.id]["Info"]
				@team_sev_hash[each_team.id] = {"Total"=> total_count,"Critical" => corpo_find_hash[each_team.id]["Critical"],"High"=>corpo_find_hash[each_team.id]["High"],"Medium"=>corpo_find_hash[each_team.id]["Medium"],"Low"=>corpo_find_hash[each_team.id]["Low"],"Info"=>corpo_find_hash[each_team.id]["Info"]}
			else
				@team_sev_hash[each_team.id] =  {"Total"=>0,"Critical" => 0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0} 
			end
			@team_names_with_sev_hash[each_team.name] = @team_sev_hash[each_team.id]
			@team_names_with_sev_hash[each_team.name]["repos"] =  each_team.repos.map(&:name).join(",")

		end
		#	respond_to do |format|
		#		format.html
		#		format.json { render json: TeamsDatatable.new(view_context,current_user) }
		#	end	
	end
	def fetch_projects
		@team = Team.find_by_name(params[:name])  
		@projects = @team.repos
	end
	def list_groups
		if !current_user.is_admin?
			unauthorized_page
		else
			@team_active_path = "/teams/list_groups"
			respond_to do |format|
				format.html
				format.json { render json: GroupsDatatable.new(view_context) }
			end
		end
	end
	def new
		unauthorized_page unless current_user.is_admin?
		@team = Team.new
	end
	def create
		@team = Team.new(team_params)

		respond_to do |format|
			if @team.save
			#	params[:team][:role_ids].select { |id| id.present? }.each do |role|
			#		role_obj = Role.find(role)
			#		@team.roles << role_obj
			#	end
				format.html { redirect_to list_groups_teams_path,method: 'GET' ,notice: 'Team was successfully created.' }
			else
				format.html { render :new }
				format.json { render json: @team.errors, status: :unprocessable_entity }
			end
		end
	end

	def edit
		unauthorized_page unless current_user.is_admin?
		@team_active_path = "/teams/#{params[:id]}/edit"
	end

	def update
		respond_to do |format|
			@team.name = params[:team][:name]
			#@team.role_ids = params[:team][:role_ids].select { |id| id.present? }
			if @team.save
				format.html { redirect_to list_groups_teams_path, notice: 'Team was successfully updated.' }
			else
				format.html { render :edit }
				format.json { render json: @team.errors, status: :unprocessable_entity }
			end

		end
	end

	def destroy
		unauthorized_page unless current_user.is_admin? 
		@team.destroy
		respond_to do |format|
			format.html { redirect_to list_groups_teams_path, notice: 'Team was successfully destroyed.' }
			format.json { head :no_content }
		end
	end
	def export_findings
		@team = Team.find(params[:team_id]) 
		projects = @team.repos
		is_first_iteration = true
		projects.each do |repo|
			findings = repo.findings
			if findings.count > 0 && is_first_iteration
				@findings = findings
				is_first_iteration = false		
			elsif findings.count > 0
				@findings = @findings | findings
			end
		end
		respond_to do |format|
			format.html
			format.csv { send_data generate_csv }
			format.xls
		end
	end
	private
	def team_params
			params.require(:team).permit(:name)
	end
	def set_group
		@team = Team.find(params[:id])
	end
	def setup_repo
		@repo = Repo.find(params[:repo_id])
	end
end
