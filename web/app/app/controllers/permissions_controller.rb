class PermissionsController < ApplicationController
	load_and_authorize_resource
	before_action :set_permission, only: [:show, :edit, :update, :destroy]

	# GET /permissions
	# GET /permissions.json
	def index
		@permissions = Permission.all
	end

	# GET /permissions/1
	# GET /permissions/1.json
	def show
	end

	# GET /permissions/new
	def new
		@permission = Permission.new
	end

	# GET /permissions/1/edit
	def edit
	end

	# POST /permissions
	# POST /permissions.json
	def create
		@permission = Permission.new(permission_params)
		@permission.status = 'Active'
		respond_to do |format|
			if @permission.save
				@msg = 'operation was successfully created.'
				@permissions_count = Permission.count

			else
				@msg = 'Operation was successfully created.'
			end
			format.js
		end
	end

	# PATCH/PUT /permissions/1
	# PATCH/PUT /permissions/1.json
	def update
		respond_to do |format|
			if @permission.update(permission_params)
				format.html { redirect_to @permission, notice: 'Permission was successfully updated.' }
				format.json { render :show, status: :ok, location: @permission }
			else
				format.html { render :edit }
				format.json { render json: @permission.errors, status: :unprocessable_entity }
			end
		end
	end

	# DELETE /permissions/1
	# DELETE /permissions/1.json
	def destroy
		@permission.destroy
		respond_to do |format|
			format.html { redirect_to permissions_url, notice: 'Permission was successfully destroyed.' }
			format.json { head :no_content }
		end
	end

	private
	# Use callbacks to share common setup or constraints between actions.
	def set_permission
		@permission = Permission.find(params[:id])
	end

	# Never trust parameters from the scary internet, only allow the white list through.
	def permission_params
		params.require(:permission).permit(:name, :status)
	end
end
