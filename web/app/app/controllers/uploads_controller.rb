class UploadsController < ApplicationController
	before_action :set_finding,except: [:download_file]
	def create
		respond_to do |format|
			begin
				@upload = @finding.uploads.build(upload_params)
				@upload.user = current_user
				@uploads = @finding.uploads
				if @upload.save
					@uploads_count = @finding.uploads.count
					flash[:notice] = "File uploaded Successfully"
				else
					flash[:error] = "Invalid file format!"
				end
			rescue =>e
				flash[:error] = "Invalid file format!"
			end
			format.js
		end	
	end
	def download_file
		upload = Upload.find(params[:upload_id])
		send_file(upload.file.path,:filename => upload.file_file_name,:type => upload.file.content_type,:disposition => 'attachment')
	end
	private
	def set_finding
		@finding = Finding.find(params[:finding_id])
	end
	def upload_params
		params.require(:upload).permit(:file,:finding_id)
	end
end
