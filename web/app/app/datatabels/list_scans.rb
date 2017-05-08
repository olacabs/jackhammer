class ListScans
	delegate :params,:link_to, to: :@view

	def initialize(view,is_scan_read_present,is_scan_create_present,is_scan_delete_present,current_user)
		@view = view
		@is_scan_read_present = is_scan_read_present
		@is_scan_create_present = is_scan_create_present
		@is_scan_delete_present = is_scan_delete_present
		@current_user = current_user
	end

	def as_json(options = {})
		{
			sEcho: params[:sEcho].to_i,
			iTotalRecords: scans.count,
			iTotalDisplayRecords: scans.count,
			aaData: data
		}
	end

	private

	def data
		scans.map do |scan|
			get_target_details(scan)
		end
	end
	def scans
		if params[:owner_type] == AppConstants::OwnerTypes::CORPORATE
			if params[:is_scheduled].present?
				@scans = Scaner.includes(:findings,:repo).corporate_scans.where.not(periodic_schedule: "")
			elsif params[:scan_type] == AppConstants::ScanTypes::UPLOADED
				@scans = Scaner.includes(:findings,:repo).uploaded_corporate_scans
			else
				@scans = Scaner.includes(:findings,:repo).corporate_scans_by_scan_type(params[:scan_type])
			end
		elsif  params[:owner_type] == AppConstants::OwnerTypes::TEAM
			team_ids = @current_user.teams.map(&:id) 
			team_ids = team_ids | [Team.where(name: AppConstants::ScanTypes::WORDPRESS,is_default_team: true).first.id] if params[:scan_type] == AppConstants::ScanTypes::WORDPRESS
			team_ids = team_ids | [Team.where(name: AppConstants::ScanTypes::NETWORK,is_default_team: true).first.id] if params[:scan_type] == AppConstants::ScanTypes::NETWORK
			if params[:is_scheduled].present?
				@scans = Scaner.includes(:findings,:repo).team_scans(team_ids).where.not(periodic_schedule: "")
			elsif params[:scan_type] == AppConstants::ScanTypes::UPLOADED
				@scans = Scaner.includes(:findings,:repo).uploaded_team_scans(team_ids)
			else	
				@scans = Scaner.includes(:findings,:repo).team_scans_by_scan_type(team_ids,params[:scan_type])
			end
		else
			if params[:is_scheduled].present?
				@scans = Scaner.includes(:findings,:repo).personal_scans(@current_user.id).where.not(periodic_schedule: "")
			elsif params[:scan_type] == AppConstants::ScanTypes::UPLOADED
				#@scans = Scaner.includes(:findings,:repo).uploaded_personal_scans(@current_user.id)
			else
				@scans = Scaner.includes(:findings,:repo).personal_scans_by_scan_type(@current_user.id,params[:scan_type])
			end
		end
		@scans = @scans.order("created_at DESC")
		@scans = @scans.page(page).per_page(per_page)
		if params[:sSearch].present?
			@search_result = Array.new
			@scans.each do |each_scan|
				@search_result << each_scan if (each_scan.project_title.present? && each_scan.project_title.downcase.include?(params[:sSearch].downcase)) || ( each_scan.repo.present? && each_scan.repo.name.downcase.include?(params[:sSearch].downcase))
			end
			@scans = @search_result

		end
		@scans
	end
	def get_target_details(scan)
		sev_counts = scan.findings.group_by_severity
		sev_hash = {"Critical"=>0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0}
		sev_counts.each do |each_count|
                        sev_hash[each_count[0]] = each_count[1]
                end
		record = Array.new
		project_title = scan.project_title.present? ? scan.project_title : scan.repo.try(:name)
		repo_url = scan.target.present? ? scan.target : scan.project_target.present? ? scan.project_target : scan.repo.try(:ssh_repo_url)
		findings = scan.total_count
		check_box = @view.check_box(:scan,"id_#{scan.id}".to_sym,checked: false,onclick: "compare_scans(#{scan.id},'#{repo_url}')")
		record << check_box
		title = findings > 0 ? link_to(project_title,@view.repo_summary_repos_path(repo_id: scan.repo.id,scan_type: scan.scan_type,owner_type: params[:owner_type],page: "scans")) : project_title
		record << ERB::Util.h(title).html_safe
		record << scan.created_at.strftime("%d/%m/%y")
		if scan.is_upload_scan == true
			record << scan.project_target_file_name
		else
			repo_url = "<a href='#' data-toggle='popover' data-trigger='hover'  data-content=#{repo_url}><i class='glyphicon glyphicon-info-sign text-info'></i></a>"
			record << repo_url.html_safe
		end
		status = scan.status.try(:camelcase)
		record << status
		sev_counts = "<ul class='inline alert-badges'>
				   <li class='severity-badge light critical-badge text-center' title='Critical'>
		#{sev_hash[AppConstants::SeverityTypes::CRITICAL]}
				   </li>
				   <li class='severity-badge light high-badge text-center' title='High'>
		#{sev_hash[AppConstants::SeverityTypes::HIGH]}
				   </li>
				    <li class='severity-badge light medium-badge text-center' title='Medium'>
		#{sev_hash[AppConstants::SeverityTypes::MEDIUM]}
				    </li>
				    <li class='severity-badge light low-badge text-center' title='Low'>
		#{sev_hash[AppConstants::SeverityTypes::LOW]}
				    </li>
				    <li class='severity-badge light info-badge text-center' title='Info'>
		#{sev_hash[AppConstants::SeverityTypes::INFO]}
				     </li>
				 </ul>"
		record << ERB::Util.h(sev_counts.html_safe)
		if @is_scan_create_present && scan.is_upload_scan == false && scan.scan_type != AppConstants::ScanTypes::MOBILE
			if scan.status == 'Scanning'
				record << link_to("<button class='btn-xs btn-success' disabled='disabled'> Scanning</button>".html_safe,@view.run_scaners_path(scan_id: scan.id),method: :get, remote: true,'disabled' => 'disabled')
			else
				record << link_to(scan.is_scanned || scan.status == 'Completed' ? "<button class='btn-xs btn-success'>Re-Scan</button>".html_safe : "<button class='btn-xs btn-success'>Scan Now</button>".html_safe,@view.run_scaners_path(scan_id: scan.id),method: :get, remote: true,class: 'label-class',id: scan.id)
			end
		end
		if @is_scan_read_present || scan.owner_type == AppConstants::OwnerTypes::PERSONAL
			if findings > 0
				record << link_to("<button class='btn-xs btn-primary'>View Results</button>".html_safe,@view.findings_path(scan_id: scan.id))
			else
				record << link_to("<button class='btn-xs btn-primary'>View Results</button>".html_safe,@view.findings_path(scan_id: scan.id),"disabled": "disabled")
			end
		end
		if @is_scan_delete_present || scan.owner_type == AppConstants::OwnerTypes::PERSONAL
			record << link_to('<i class="glyphicon glyphicon-remove-circle text-danger"> </i>'.html_safe,@view.scaner_path(scan),title: 'Delete',method: :delete, remote: true,confirm: "Are you sure you want to delete this?")
		end
		record << link_to('<i class="fa fa-file-excel-o text-info"> </i>'.html_safe,@view.download_results_scaners_path(format: "xls",scan_id: scan.id),title: 'Export')
		if params[:is_scheduled].present?
			record << link_to('<i class="fa fa-stop text-danger"> </i>'.html_safe,@view.unschedule_scaners_path(scan_id: scan.id),method: :post,remote: true,title: 'Unschedule')
		end
		record
	end
	def page
		params[:iDisplayStart].to_i/per_page + 1
	end

	def per_page
		params[:iDisplayLength].to_i > 0 ? params[:iDisplayLength].to_i : 10
	end
end
