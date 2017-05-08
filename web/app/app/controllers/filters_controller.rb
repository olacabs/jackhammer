class FiltersController < ApplicationController
	def results
		get_scan_type
		get_sev_count unless params[:iDisplayStart].to_i > 0
		params[:filter][:scan_type] = @scan_types
		respond_to do |format|
			format.js
			format.json { render json: FiltersDatatable.new(view_context,current_user,session[:filter_type]) }
		end
	end
	def apply_filter
		session[:filter_type] = params[:filter_type]
		filter_options
	end
	def get_scan_type
		@scan_types = Array.new
		AppConstants::AppTypes::APP_TYPES.each do |scan_type|
			if params["filter"][scan_type.downcase + "_app"].to_i == 1
				@scan_types << scan_type
				@scan_types << AppConstants::ScanTypes::HARDCODE if scan_type == AppConstants::ScanTypes::STATIC
			end
		end
	end
	def filter_options
		@tools = Task.where(group: ['Ruby','Javascript','Java']).order(:name)
		@repos = Repo.order(:name)
		@teams = Team.order(:name)  if session[:filter_type] == 'corporate'
		@repos = current_user.repos.order(:name) unless session[:filter_type] == AppConstants::OwnerTypes::CORPORATE
		@tags = Tag.order(:name)
		@tasks = Task.where("scan_type_id is not NULL")
		@severity_levels = Task.where(group: 'Severity')
		@vul_types = Finding.uniq.pluck(:description)
	end
	def get_sev_count
		filter_obj = FiltersDatatable.new(view_context,current_user,session[:filter_type])
		where_clause = filter_obj.get_where_clause
		tag_ids = params[:filter][:tag_id].reject(&:blank?)
		findings = Finding.by_scan_type(@scan_types)
		findings = findings.by_corporate if session[:filter_type] == AppConstants::OwnerTypes::CORPORATE
		findings = findings.joins(:tags).where("tags.id in (?)",tag_ids ) if tag_ids.present?
		findings = findings.where("date(findings.created_at) >= ?", params[:filter][:from_date].to_date) if params[:filter][:from_date].present?
		findings = findings.where("date(findings.created_at) <= ?", params[:filter][:to_date].to_date) if params[:filter][:to_date].present?
		#findings = findings.where("date(created_at) between ? and ?", params[:filter][:from_date].to_date,params[:filter][:to_date].to_date) if params[:filter][:from_date].present? && params[:filter][:to_date].present?
		aging = params[:filter][:aging]
		findings = findings.where("findings.created_at between ? and ?" ,((Date.today - aging.to_i).to_s + " " + "00:00").to_time(:utc),(Date.today.to_s + " " + "23:59").to_time(:utc)) if aging.present?
		#findings = findings.where("date(created_at) between ? and ?",(Date.today.to_date - aging.to_i),Date.today) if aging.present?
		filter_finds = findings.uniq.filter_finds(where_clause).uniq
		@findings_hash = init_severity_hash 
		filter_finds.each { |find| @findings_hash[find.severity] += 1 }
	end
end
