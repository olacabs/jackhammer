class RolesController < ApplicationController
        load_and_authorize_resource
	before_action :set_role, only: [:show, :edit, :update, :destroy]

	def index
		unauthorized_page unless current_user.is_admin?
		@roles_active_path = "/roles" 
		@roles = Role.all
	end
	#GET /functionalities/new
	def new
		@roles_active_path = "/roles/new" 
		@role = Role.new
	end
        def show
	 	    @roles_active_path = "/roles/#{params[:id]}" 
        end
	def create
		@role = Role.new(role_params) #,function_operations: get_function_operations)
		respond_to do |format|
			if @role.save
			#	params[:role][:permission_ids].select { |id| id.present? }.each do |permission|
			#		permission_obj = Permission.find(permission)
			#		@role.permissions << permission_obj
			#	end
                                
                 		@opted_functions.each do |functionality|
					functionality_obj = Functionality.find(functionality)
					@role.functionalities << functionality_obj
				end
				format.html { redirect_to roles_path,method: 'GET' ,notice: 'Role was successfully created.' }
			else
				format.html { render :new }
				format.json { render json: @role.errors, status: :unprocessable_entity }
			end
		end
	end
        def edit
		@roles_active_path = "/roles/#{params[:id]}/edit"
	end
	def update
		respond_to do |format|
			@role.name = params[:role][:name]
			@role.functionality_ids = params[:role][:functionality_ids]
                        @role.function_operations = get_function_operations 
                        #@role.permission_ids = params[:role][:permission_ids]
			if @role.save
				format.html { redirect_to(@role, :notice => 'Role was successfully updated.') }
				format.json { render :show, status: :ok, location: @role }
			else
				format.html { render :action => "edit" }
				format.json { respond_with_bip(@role) }
			end
		end
	end
	def destroy
		@role.destroy
		respond_to do |format|
			format.html { redirect_to roles_url, notice: 'Roles was successfully destroyed.' }
			format.json { head :no_content }
		end
	end
	
        private
	def role_params
                params[:role][:function_operations] = get_function_operations
		@opted_functions = params[:role][:functionality_ids].select { |id| id.present? } 
		params.require(:role).permit(:name,:permissions,:function_operations,:functionality_ids)
	end
        def get_function_operations
            selected_operations = Array.new
            if params[:role][:permissions].present?
                params[:role][:permissions].each { |key,value| selected_operations << key if value == "1" }
            end
            selected_operations.join(",")
        end
	def set_role
            @role = Role.find(params[:id])
	end
      #  def get_function_operations
      #      selected_operations = Array.new
      #      if params[:role][:permissions].present?
      #          params[:role][:permissions].each { |key,value| selected_operations << key if value == "1" }  
      #      end
      #      selected_operations.join(",")
      #  end
end
