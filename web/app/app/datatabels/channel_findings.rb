class ChannelFindings
	delegate :params,:link_to, to: :@view

	def initialize(view,scaner,channel,created_date)
		@view = view
		@scan = scaner
		@channel = channel
		@created_date = created_date
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
		findings.map do |finding|
			[
				ERB::Util.h(finding.description),
				ERB::Util.h("<span class='#{finding.severity.downcase}-row badge finds-row'>#{finding.severity.classify}".html_safe),
				ERB::Util.h(finding.issue_type),
				link_to('View Finding',@view.details_findings_path(finding_id: finding.id,target: '_blank'))
			]
		end
	end

	def findings
		@findings ||= fetch_findings
	end
	def fetch_findings
		findings = @scan.findings.where("lower(scanner) in (?) and date(created_at) = ? ",@channel.downcase,@created_date.to_date).order("#{sort_column} #{sort_direction}")
		findings = findings.page(page).per_page(per_page)
		if params[:sSearch].present?
			findings = findings.where("lower(severity) like :search or lower(description) like :search or lower(scanner) like :search", search: "%#{params[:sSearch].downcase}%")
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
		columns = %w[description severity]
		columns[params[:iSortCol_0].to_i]
	end

	def sort_direction
		params[:sSortDir_0] == "desc" ? "desc" : "asc"
	end
end
