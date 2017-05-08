class FindingsDatatable
	delegate :params,:link_to, to: :@view

	def initialize(view,scaner,current_user)
		@view = view
		@scan = scaner
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
			find_results = admin_findings
		else    
			find_results = user_findings
		end             
		find_results
	end
	def admin_findings
		findings.map do |finding|
			[

				ERB::Util.h("<span class='#{finding.severity.downcase}-row badge finds-row'>#{finding.severity.classify}".html_safe),
				ERB::Util.h(finding.description),
				ERB::Util.h(finding.scanner),  
				ERB::Util.h(finding.issue_type), 
				link_to('View Finding',@view.details_findings_path(finding_id: finding.id,scan_type: @scan_type,page: "list_findings"))
			]                               
		end  
	end     
	def user_findings
		findings.map do |finding|
			[

				ERB::Util.h("<span class='#{finding.severity.downcase}-row badge finds-row'>#{finding.severity.classify}".html_safe),
				ERB::Util.h(finding.description),
				ERB::Util.h(finding.issue_type),
				link_to('View Finding',@view.details_findings_path(finding_id: finding.id,scan_type: @scan_type,page: "list_findings"))
			]                               
		end  
	end
	def findings
		@findings ||= fetch_findings
	end
	def fetch_findings
		findings = @scan.findings
		@severity_levels = @current_user.tasks.where(group: 'Severity').map(&:name)
		findings = findings.order("#{sort_column} #{sort_direction}")
		findings = findings.page(page).per_page(per_page)
		if params[:sSearch].present?
			findings = findings.where("upper(cast(severity as char)) like :search or upper(cast(description as char)) like :search or upper(cast(scanner as char)) like :search", search: "%#{params[:sSearch].upcase}%")
		end
		findings
	end

	def page
		params[:iDisplayStart].to_i/per_page + 1
	end

	def per_page
		params[:iDisplayLength].to_i > 0 ? params[:iDisplayLength].to_i : 10
	end

	def sort_column
		columns = %w[severity  description scanner]
		columns[params[:iSortCol_0].to_i]
	end

	def sort_direction
		params[:sSortDir_0] == "desc" ? "desc" : "asc"
	end
end
