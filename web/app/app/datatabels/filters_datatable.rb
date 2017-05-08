class FiltersDatatable
	delegate :params,:link_to, to: :@view

	def initialize(view,current_user,filter_type)
		@view = view
		@current_user = current_user
		@filter_type = filter_type
	end

	def as_json(options = {})
		{
			sEcho: params[:sEcho].to_i,
			iTotalRecords: findings.count,
			iTotalDisplayRecords: findings.count,
			aaData: data
		}
	end


	def data

		if @current_user.is_admin? || @current_user.is_security_member?
			findings.map do |finding|
				[
					ERB::Util.h("<input id=#{finding.id} class='styled' type='checkbox'>".html_safe).html_safe,
					ERB::Util.h("<span class='#{finding.severity.downcase}-row badge finds-row'>#{finding.severity.classify}".html_safe),
					ERB::Util.h(finding.description.present? ? finding.description.truncate(20) : ""),
					ERB::Util.h(finding.scanner),
					ERB::Util.h(finding.created_at.strftime("%d-%m-%y %H:%M:%S")),
					ERB::Util.h(finding.updated_at.strftime("%d-%m-%y %H:%M:%S")),
					ERB::Util.h(finding.try(:repo).try(:name).present? ? finding.repo.name.truncate(15) : ""),
				        link_to('View Finding',@view.details_findings_path(finding_id: finding.id,page: 'filters'),target: '_blank')
				]
			end
		else
			findings.map do |finding|
				[
					ERB::Util.h("<input id=#{finding.id} class='styled' type='checkbox'>".html_safe).html_safe,
					ERB::Util.h("<span class='#{finding.severity.downcase}-row badge finds-row'>#{finding.severity.classify}".html_safe),
					ERB::Util.h(finding.description.present? ? finding.description.truncate(20) : ""),
					ERB::Util.h(finding.created_at.strftime("%d-%m-%y %H:%M:%S")),
					ERB::Util.h(finding.updated_at.strftime("%d-%m-%y %H:%M:%S")),
					ERB::Util.h(finding.try(:repo).try(:name).present? ? finding.repo.name.truncate(15) : ""),
					link_to('View Finding',@view.details_findings_path(finding_id: finding.id,page: 'filters'),target: '_blank')
				]
			end

		end
	end
	def findings
		@findings ||= fetch_findings
	end
	def fetch_findings
		where_clause = get_where_clause
		findings = Finding.where(where_clause).by_scan_type(params[:filter][:scan_type]).includes(:repo)
		findings = findings.by_corporate if @filter_type == AppConstants::OwnerTypes::CORPORATE
		findings = findings.where("date(findings.created_at) >= ?", params[:filter][:from_date].to_date) if params[:filter][:from_date].present?
                findings = findings.where("date(findings.created_at) <= ?", params[:filter][:to_date].to_date) if params[:filter][:to_date].present?
		#findings = findings.where("date(created_at) between ? and ?", params[:filter][:from_date].to_date,params[:filter][:to_date].to_date) if params[:filter][:from_date].present? && params[:filter][:to_date].present?
		aging = params[:filter][:aging]
		findings = findings.where("findings.created_at between ? and ?" ,((Date.today - aging.to_i).to_s + " " + "00:00").to_time(:utc),(Date.today.to_s + " " + "23:59").to_time(:utc)) if aging.present?
		#findings = findings.where("date(created_at) between ? and ?",(Date.today.to_date - aging.to_i),Date.today) if aging.present?
		tag_ids = params[:filter][:tag_id].reject(&:blank?)
		findings = findings.joins(:tags).where("tags.id in(?)", tag_ids) if tag_ids.present?
		findings = findings.order("#{sort_column} #{sort_direction}")
		findings = findings.uniq.page(page).per_page(per_page)
		if params[:sSearch].present?
			findings = findings.where("upper(cast(severity as char)) like :search or upper(cast(description as char)) like :search or upper(cast(scanner as char)) like :search", search: "%#{params[:sSearch].upcase}%")
		end
		findings
	end
	def get_where_clause
		tools = Setting.pipeline['enabled_tools']
		if tools
			tools  = AppConstants::TOOLS::DEFAULT_EXTERNAL_TOOLS + tools.split(",")
		else    
			tools  = AppConstants::TOOLS::DEFAULT_EXTERNAL_TOOLS
		end
		filters = [:team_id, :repo_id,:description,:status,:severity]
		wheres = filters.map{|key| params[:filter].has_key?(key) ? {key => params[:filter][key].reject(&:blank?)} : {} }
		.inject({}){|hash, injected| hash.merge!(injected)}
		wheres = wheres.select {|k,v| v.present? }
		select_tools = ""
		select_tools = params[:filter][:scanner].reject{ |scanner| scanner.empty?} if  params[:filter][:scanner].present?
		tools = select_tools if select_tools.present?
		selected_status = params[:filter][:status].reject{ |status| status.empty?}
		if selected_status.present?
			wheres.merge!(is_false_positive: false,status: selected_status,scanner: tools.split(","))
		else
			wheres.merge!(is_false_positive: false,status: [AppConstants::FindingStatus::NEW,AppConstants::FindingStatus::OPEN,AppConstants::FindingStatus::FIX_IN_PROGRESS,AppConstants::FindingStatus::DEFERRED],scanner: tools.split(","))
		end
		wheres.merge!(user_id: @current_user.id,team_id: nil) unless @filter_type == AppConstants::OwnerTypes::CORPORATE
		wheres
	end
	private

	def page
		params[:iDisplayStart].to_i/per_page + 1
	end

	def per_page
		params[:iDisplayLength].to_i > 0 ? params[:iDisplayLength].to_i : 10
	end

	def sort_column
		columns = %w[severity severity  description created_at updated_at repos.name]
		columns[params[:iSortCol_0].to_i]
	end

	def sort_direction
		params[:sSortDir_0] == "desc" ? "desc" : "asc"
	end
end
