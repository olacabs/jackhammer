module FiltersHelper
	def is_static_enabled
		return true unless params[:filters].present?
		return false if params[:filters][:is_static].to_i == 0
		return true
	end

end
