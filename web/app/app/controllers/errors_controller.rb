class ErrorsController < ApplicationController
	def show
		render status_code.to_s, :status => status_code
	end
	def unauthorized

	end
	def not_found

	end
	def render_500

	end
	protected

	def status_code
		params[:code] || 500
	end
end
