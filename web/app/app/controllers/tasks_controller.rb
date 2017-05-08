class TasksController < ApplicationController
	before_action :set_task, only: [:show, :edit, :update, :destroy]

	# GET /tasks
	# GET /tasks.json
	def index
		@list_scans_class = "/tasks"
		@tasks = Task.where.not(scan_type: nil).order(:group)
	end

	# GET /tasks/1
	# GET /tasks/1.json
	def show
	end

	# GET /tasks/new
	def new
		@list_scans_class = "/tasks/new"
		@task = Task.new
	end

	# GET /tasks/1/edit
	def edit
		@list_scans_class = "/tasks/#{params[:id]}/edit"
	end

	# POST /tasks
	# POST /tasks.json
	def create
		@task = Task.new(task_params)

		respond_to do |format|
			if @task.save
				format.html { redirect_to tasks_url, notice: 'Task was successfully created.' }
				format.json { render :show, status: :created, location: @task }
			else
				format.html { render :new }
				format.json { render json: @task.errors, status: :unprocessable_entity }
			end
		end
	end

	# PATCH/PUT /tasks/1
	# PATCH/PUT /tasks/1.json
	def update
		respond_to do |format|
			if @task.update(task_params)
				format.html { redirect_to tasks_url, notice: 'Task was successfully updated.' }
				format.json { render :show, status: :ok, location: @task }
			else
				format.html { render :edit }
				format.json { render json: @task.errors, status: :unprocessable_entity }
			end
		end
	end

	# DELETE /tasks/1
	# DELETE /tasks/1.json
	def destroy
		@list_scans_class = "/settings/scanner"
		@task.destroy
		respond_to do |format|
			format.html { redirect_to tasks_url, notice: 'Task was successfully destroyed.' }
			format.json { head :no_content }
		end
	end
	def tools_list
		@list_scans_class = "/tasks/tools_list"
		@tasks = Task.where("scan_type_id is not NULL")
	end
	def update_tool
		@task = Task.find(params[:taks_id])
		if @task.gem_name.present?
			gems = @task.gem_name.split(",")
			gems.each do |gem|
				cmd = system("gem","update","#{gem}")
				if cmd == true
					@msg = "Upgradation successfully done."
					logger.info "#{gem} upgradation successfully done for #{gem}"
				else
					AlertNotification.create(user_id: current_user.id,identifier: 'tasks',alert_type: 'danger',message: "#{@task.name} upgradation is failed")
					logger.error "Some error happend while upgrading tool #{gem}"
					@msg = "Upgradation got failed."
				end
			end
		end
		respond_to do |format|
			format.js
		end
	end
	def configure_view
		@list_scans_class = '/tasks/configure_view'
		@commits_depth = Setting.pipeline['commits_depth']
		@commit_start_date = Setting.pipeline['commit_start_date']
		@regx_file = Dir.glob(File.join(Setting.pipeline["truffle_hog_path"],"regx_config_*")).select  {|f| File.file? f }.sort_by {|f| File.mtime f }.last
		@check_marx_server = Setting.pipeline['checkmarx_server']
		@check_marx_password = Setting.pipeline['checkmarx_password']
		@check_marx_user = Setting.pipeline['checkmarx_user']
		@check_marx_log = Setting.pipeline['checkmarx_log']
	end
	def update_configurations
		update_settings
                respond_to do |format|
                        flash[:notice] = "configurations updated successfully"
                        format.html { redirect_to :action=>"configure_view"}
                end
        end
	private
	# Use callbacks to share common setup or constraints between actions.
	def set_task
		@task = Task.find(params[:id])
	end

	# Never trust parameters from the scary internet, only allow the white list through.
	def task_params
		params.require(:task).permit(:name, :group, :scan_type_id,:gem_name)
	end
end
