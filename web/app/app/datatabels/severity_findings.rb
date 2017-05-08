class SeverityFindings
	delegate :params,:link_to, to: :@view

	def initialize(view,object,severity_level,current_user)
		@view = view
		@object = object
		@severity_level = severity_level
		@current_user = current_user
	end

	def as_json(options = {})
		{
			sEcho: params[:sEcho].to_i,
			iTotalRecords: findings.count,
			iTotalDisplayRecords: findings.count,
			aaData: data
		}
	end

	private

	def data
		if @current_user.is_admin?
			@find_results = admin_findings 
		else
			@find_results = user_findings
		end
		@find_results
	end
	def admin_findings
		findings.map do |finding|
			[
				ERB::Util.h("<input id=#{finding.id} class='styled' type='checkbox'>".html_safe).html_safe,
				ERB::Util.h(finding.description),
					ERB::Util.h(finding.scanner),
					ERB::Util.h(finding.issue_type),
					link_to('View Finding',@view.details_findings_path(finding_id: finding.id),target: '_blank')
			]
		end
	end
	def user_findings
		findings.map do |finding|
			[
				ERB::Util.h("<input id=#{finding.id} class='styled' type='checkbox'>".html_safe).html_safe,
				ERB::Util.h(finding.description),                                                       
					ERB::Util.h(finding.issue_type),                                                        
					link_to('View Finding',@view.details_findings_path(finding_id: finding.id),target: '_blank')
			]
		end   
	end
	def findings
		@findings ||= fetch_findings
	end
	def fetch_findings
		@all_findings = get_findings(@object)
		@all_findings = @all_findings.order("#{sort_column} #{sort_direction}")
		@all_findings = @all_findings.page(page).per_page(per_page)
		if params[:sSearch].present?
			@all_findings = @all_findings.where("upper(cast(severity as char)) like :search or upper(cast(description as char)) like :search or upper(cast(scanner as char)) like :search", search: "%#{params[:sSearch].upcase}%")
		end
		@all_findings
	end
	def get_findings(scan)
		 scan_types = params[:scan_type] == AppConstants::ScanTypes::STATIC || params[:scan_type] == AppConstants::ScanTypes::HARDCODE ? [AppConstants::ScanTypes::STATIC,AppConstants::ScanTypes::HARDCODE] : params[:scan_type]
		@scaners_levels = Setting.pipeline['enabled_tools'].present? ? Setting.pipeline['enabled_tools'].split(",") : ""
		if @scaners_levels.present?
			@scaners_levels  = AppConstants::TOOLS::DEFAULT_EXTERNAL_TOOLS + @scaners_levels 
		else            
			@scaners_levels  = AppConstants::TOOLS::DEFAULT_EXTERNAL_TOOLS
		end  
		if params[:owner_type] == "corporate" || params[:owner_type] == "team"
			if params[:false_finds].present?
				@findings = Finding.by_corporate.by_repo_id(params[:repo_id]).by_false_positive.by_sev(@severity_level).by_tools(@scaners_levels).by_scan_type(scan_types)
			else
				@findings = Finding.by_corporate.by_repo_id(params[:repo_id]).not_false_positive.by_sev(@severity_level).by_tools(@scaners_levels).by_scan_type(scan_types)
			end
		else
			if params[:false_finds].present?
				@findings = Finding.by_not_corporate.by_repo_id(params[:repo_id]).by_false_positive.by_sev(@severity_level).by_tools(@scaners_levels).by_scan_type(scan_types).by_user(@current_user.id)
			else
				@findings = Finding.by_not_corporate.by_repo_id(params[:repo_id]).not_false_positive.by_sev(@severity_level).by_tools(@scaners_levels).by_scan_type(scan_types).by_user(@current_user.id)
			end
		end
		@findings
	end
	def page
		params[:iDisplayStart].to_i/per_page + 1
	end

	def per_page
		params[:iDisplayLength].to_i > 0 ? params[:iDisplayLength].to_i : 10
	end

	def sort_column
		columns = %w[description description scanner]
		columns[params[:iSortCol_0].to_i]
	end

	def sort_direction
		params[:sSortDir_0] == "desc" ? "desc" : "asc"
	end
end
