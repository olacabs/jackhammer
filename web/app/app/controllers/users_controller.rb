class UsersController < ApplicationController
	load_and_authorize_resource
	before_action :set_user, only: [:edit, :update, :destroy]

	def index
		@user_active_path = "/users"
		respond_to do |format|
			format.html
			format.json { render json: UsersDatatable.new(view_context,current_user) }
		end
	end

	def new
		@user_active_path = "/users/new"
		get_users
		@user = User.new	
		#@users = User.all.order(:name)
		#get_repos
	end

	def add 
		team = current_user_teams.first
		user = User.find(params[:user][:id])
		team.users << user 
		#user.repo_ids = params[:user][:repo_ids].select { |repo| repo.present? }	
		respond_to do |format|
			format.html { redirect_to users_path, notice: 'User was added successfully.',method: 'GET'  }
		end
	end

	def edit
		@user_active_path = "/users/#{params[:id]}/edit"
		#get_repos
	end

	def update
		if current_user.is_admin?
			@is_team_lead = params[:user][:is_team_lead].to_i
			@is_admin = params[:user][:is_admin].to_i
			@is_security_member = params[:user][:is_security_member].to_i
		elsif current_user.is_team_lead?
			@is_team_lead = false
		end
		@user.update(is_team_lead?: @is_team_lead,name: params[:user][:name],is_admin?: @is_admin,is_security_member?: @is_security_member)
		@user.repo_ids = params[:user][:repo_ids].select { |repo| repo.present? } if params[:user][:repo_ids].present?
		@user.role_ids = params[:user][:role_ids].select { |role| role.present? } if params[:user][:role_ids].present?
		selected_teams = params[:user][:team_ids].reject(&:blank?)
		if selected_teams.present?
			@user.team_ids = selected_teams
		end
		respond_to do |format|
			if @user.save
				format.html { redirect_to users_path, notice: 'User details was successfully updated.',method: 'GET'  }
			else
				format.html { render :edit }
				format.json { render json: @user.errors, status: :unprocessable_entity }
			end
		end
	end

	def destroy
		if !current_user.is_admin?
			unauthorized_page 
		else
			@user.destroy 
			respond_to do |format|
				format.html { redirect_to users_url, notice: 'User was successfully destroyed.' }
				format.json { head :no_content }
			end
		end
	end

	private
	def set_user
		@user = User.find(params[:id])
	end
	def get_repos
		if !current_user.is_admin?
			@teams = current_user_teams
			@projects = @teams.first.repos
			count = 0
			@teams.each do |team|
				count = count + 1
				next if count == 1
				@projects = @projects | team.repos
			end
		else
			@projects = Repo.all
		end
	end
	def get_users
		@teams = current_user_teams
		user_ids = Array.new
		@teams.each do |team|
			user_ids << team.users.map(&:id)
		end
		@users = User.where.not(id: user_ids).where(is_admin?: false).order(:name)
	end
	def current_user_teams
		current_user.teams 
	end
end
