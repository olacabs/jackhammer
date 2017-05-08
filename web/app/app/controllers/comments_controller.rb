class CommentsController < ApplicationController
	before_action :set_finding
	def index
		@comments = @finding.comments
		@comment = Comment.new
	end
	def create
		@comment = @finding.comments.build(comment_params)
		@comment.user = current_user
		@comments = @finding.comments
		logger.info "comment by user #{current_user.name} for finding #{@finding.id}"
		respond_to do |format|
			if @comment.save
				@comments_count = @finding.comments.count
				flash[:notice] = "Comment created successfully"
				format.js
			else
				flash[:error] = "Comment creation failed!"
				format.js
			end
		end

	end
	private
	def set_finding
		@finding = Finding.find(params[:finding_id])
	end
	def comment_params
		params.require( :comment ).permit(:message,:finding_id,{:tag_ids=>[]})		
	end
end
