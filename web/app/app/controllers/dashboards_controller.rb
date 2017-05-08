class DashboardsController < ApplicationController
	def display
		if  params[:scan_type] == AppConstants::ScanTypes::STATIC 
			@app_type =  'Source Code Apps'
			@scan_types = [AppConstants::ScanTypes::STATIC,AppConstants::ScanTypes::HARDCODE]
		else
			@scan_types = [params[:scan_type]]
			@app_type =   params[:scan_type] + "Apps"
		end
		get_user_settings
		get_findings(@scan_types)
		initilize_apps_values
		get_trending_values
		get_apps_sev_count(@scan_types)
		get_apps_critical_count
		get_severities_count
		render 'list_dashboards'
	end
	def get_findings(scan_type)
		if params[:dashboard_type] == AppConstants::OwnerTypes::CORPORATE
			@month_wise_findings = Finding.by_corporate.not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).group_by_month
			@high_vul_apps = Finding.by_corporate.not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).top_vul_repos(5)
			if @severity_levels.include?(AppConstants::SeverityTypes::CRITICAL)
				@top_critical = Finding.by_corporate.not_false_positive.by_sev(AppConstants::SeverityTypes::CRITICAL).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).top_vul_repos(10)
			else
				@top_critical = []
			end
			@sev_counts = Finding.by_corporate.not_false_positive.by_tools(@scaners_levels).by_sev(@severity_levels).by_scan_type(scan_type).group_by_severity
		elsif params[:dashboard_type] == AppConstants::OwnerTypes::TEAM
			@team_ids = current_user.teams.map(&:id)
			@month_wise_findings = Finding.by_corporate.not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).by_teams(@team_ids).group_by_month
			@high_vul_apps = Finding.by_corporate.not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).by_teams(@team_ids).top_vul_repos(5)
			if @severity_levels.include?(AppConstants::SeverityTypes::CRITICAL)
				@top_critical = Finding.by_corporate.not_false_positive.by_sev(AppConstants::SeverityTypes::CRITICAL).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).by_teams(@team_ids).top_vul_repos(10)
			else
				@top_critical = []
			end
			@sev_counts = Finding.by_corporate.not_false_positive.by_tools(@scaners_levels).by_sev(@severity_levels).by_scan_type(scan_type).by_teams(@team_ids).group_by_severity
		else
			@month_wise_findings = Finding.by_not_corporate.not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).by_user(current_user.id).group_by_month
			@high_vul_apps = Finding.by_not_corporate.not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).by_user(current_user.id).top_vul_repos(5)
			if @severity_levels.include?(AppConstants::SeverityTypes::CRITICAL)
				@top_critical = Finding.by_not_corporate.not_false_positive.by_sev(AppConstants::SeverityTypes::CRITICAL).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).by_user(current_user.id).top_vul_repos(5)
			else
				@top_critical = []
			end
			@sev_counts = Finding.by_not_corporate.not_false_positive.by_tools(@scaners_levels).by_sev(@severity_levels).by_scan_type(scan_type).by_user(current_user.id).group_by_severity
		end
	end
	def initilize_apps_values
		@most_vul_apps = Hash.new
		@critical_apps = Hash.new
		@month_hash = get_last_one_years_moths
		@top_apps = Array.new
		@apps_sev_finds = {"Critical"=> [],"High" => [],"Medium"=> [], "Low" => [],"Info" => []}
		@sev_hash = {"Critical"=>0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0}
	end
	def get_trending_values
		@month_wise_findings.each { |key,count| @month_hash[Time.zone.local(key[0],key[1],1, 0, 0, 0).strftime("%b-%y")][key[2]] = count }
		for each_month in 0..@month_hash.keys.length-1
			if each_month != 0
				severity_levels.each do |each_sev|
					@month_hash[@month_hash.keys[each_month]][each_sev]  += @month_hash[@month_hash.keys[each_month-1]][each_sev]
				end
			end
		end
		get_findings_array
	end
	def get_apps_sev_count(scan_type)
		@high_vul_apps.each do |each_repo|
			repo = Repo.find(each_repo[0])
			@top_apps << repo.name
			if params[:dashboard_type] == AppConstants::OwnerTypes::CORPORATE
				@repo_findings = Finding.by_corporate.not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).by_repo_id(each_repo[0]).group_by_severity
			elsif params[:dashboard_type] == AppConstants::OwnerTypes::TEAM
				@repo_findings = Finding.by_corporate.not_false_positive.by_sev(@severity_levels).by_tools(@scaners_levels).repo_not_blank.by_scan_type(scan_type).by_teams(@team_ids).by_repo_id(each_repo[0]).group_by_severity
			else
				@repo_findings = Finding.by_not_corporate.not_false_positive.by_tools(@scaners_levels).by_sev(@severity_levels).by_scan_type(scan_type).by_repo_id(each_repo[0]).by_user(current_user.id).group_by_severity
			end

			@repo_findings.each do |each_sev|
				@apps_sev_finds[each_sev[0]] << each_sev[1]
			end
		end
	end

	def get_apps_critical_count
		@top_critical.each do |each_repo|
			repo = Repo.find(each_repo[0])
			@critical_apps[repo.name] = each_repo[1]
		end
	end
	def get_severities_count
		@sev_counts.each do |each_count|
			@sev_hash[each_count[0]] = each_count[1]
		end
	end	
end
