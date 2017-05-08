class UploadScansController < ApplicationController
	def new
		unauthorized_page unless current_user.is_admin? || current_user.is_team_lead? || current_user.is_security_member?
		get_user_repos
	end
	def create
		@scaner = current_user.scaners.build(corporate_scanner_params)
		@scaner.invloved_tools = corporate_scanner_params[:source]
		save_scanner
	end
	def save_team
		@scaner = current_user.scaners.build(team_scanner_params)
		@scaner.invloved_tools = team_scanner_params[:source]
		repo_team = Repo.find(team_scanner_params[:repo_id]).team
		@scaner.team = repo_team
		save_scanner
	end
	def save_scanner
		@scaner.status = "Started uploading results...."
		respond_to do |format|
			if @scaner.save
				@scaner.update(is_upload_scan: true,owner_type: 'corporate')
				scaner_instance = ScanerInstance.create(scaner_id: @scaner.id)
				klass = "#{@scaner.source}UploadWorker".constantize
				klass.perform_async(@scaner.id)
				owner_type = current_user.is_team_lead? ? AppConstants::OwnerTypes::TEAM : AppConstants::OwnerTypes::CORPORATE
				flash[:notice] = "Scan Target added successfully"
				format.html { redirect_to :controller=>"scaners",:action=>"index",scan_type: AppConstants::ScanTypes::UPLOADED,owner_type: owner_type}
			else
				get_user_repos
				flash[:error] = "Only xml files are accepted"
				format.html { render 'new' }
			end
		end
	end
	def get_user_repos
		@user_repos = current_user.repos
		@user_repos = current_user.teams.first.repos if current_user.is_team_lead?
	end
	private
	def corporate_scanner_params
		params.require(:scaners).permit(:project_title,:target,:branch_name,:source,:project_target,:team_id,:repo_id,:branch_id,:periodic_schedule,:invloved_tools,:vulnerable_types,:owner_type,:scan_type)
	end
	def team_scanner_params
		params.require(:scaner).permit(:project_title,:target,:branch_name,:source,:project_target,:team_id,:repo_id,:branch_id,:periodic_schedule,:invloved_tools,:vulnerable_types,:owner_type,:scan_type)
	end
end
