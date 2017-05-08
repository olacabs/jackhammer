class ScanTypesController < ApplicationController
	before_action :set_scan_type, only: [:edit, :update, :destroy]
	def index
		unauthorized_page unless current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER
		@scan_type_class = "/scan_types"
		@scan_types = ScanType.all	
	end

	def edit
		@scan_type_class = "/scan_types/#{params[:id]}/edit"
	end

	def new
		@scan_type_class = "/scan_types/new"
		@scan_type = ScanType.new
	end
	def create
		@scan_type = ScanType.new(scan_type_params)

		respond_to do |format|
			if @scan_type.save
				format.html { redirect_to scan_types_path, notice: 'Scaner Type was successfully created.' }
				format.json { render :show, status: :created, location: @scan_type }
			else 
				format.html { render :new }
				format.json { render json: @scan_type.errors, status: :unprocessable_entity }
			end
		end
	end
	def update
		respond_to do |format|
			if @scan_type.update(scan_type_params)
				format.html { redirect_to scan_types_url, notice: 'Scaner Type was successfully updated.' }
			else
				format.html { render :edit }
			end
		end
	end
	def destroy
		@scan_type.destroy
		respond_to do |format|
			format.html { redirect_to scan_types_path, notice: 'Scaner Type was successfully destroyed.' }
			format.json { head :no_content }
		end
	end
	def set_scan_type
		@scan_type = ScanType.find(params[:id])
	end
	private
	def scan_type_params
		params.require(:scan_type).permit(:name)
	end
end
