class AnalyticsController < ApplicationController

	def show

	end
	def index
		user_id = current_user.id
		session[:analytics_type] = params[:analytics_type]
		if params[:analytics_type] == AppConstants::OwnerTypes::CORPORATE
			@severities_count = Finding.by_corporate.not_false_positive.group_by_severity
			@new_findings = Finding.by_corporate.not_false_positive.by_status(AppConstants::FindingStatus::NEW).count
			@queued_scans = Scaner.corporate_scans_by_status(AppConstants::ScanStatus::QUEUED)
			@running_scans = Scaner.corporate_scans_by_status(AppConstants::ScanStatus::SCANNING)
			@conducted_scans = Scaner.corporate_scans_by_status(AppConstants::ScanStatus::COMPLETED)
			@most_vul_repos = Finding.by_corporate.not_false_positive.repo_not_blank.by_sev([AppConstants::SeverityTypes::CRITICAL,AppConstants::SeverityTypes::HIGH]).top_vul_repos(5)
			@top_vul = Finding.by_corporate.not_false_positive.by_sev(AppConstants::SeverityTypes::CRITICAL).group_by_description
		else
			@severities_count = Finding.by_user(user_id).by_not_corporate.not_false_positive.group_by_severity
			@new_findings = Finding.by_user(user_id).by_not_corporate.by_status(AppConstants::FindingStatus::NEW).count
			@queued_scans = Scaner.personal_scans_by_status(AppConstants::ScanStatus::QUEUED,user_id)
			@running_scans = Scaner.personal_scans_by_status(AppConstants::ScanStatus::SCANNING,user_id)
			@conducted_scans = Scaner.personal_scans_by_status(AppConstants::ScanStatus::COMPLETED,user_id)
			@most_vul_repos = Finding.by_user(user_id).by_not_corporate.not_false_positive.repo_not_blank.by_sev([AppConstants::SeverityTypes::CRITICAL,AppConstants::SeverityTypes::HIGH]).top_vul_repos(5)
			@top_vul = Finding.by_user(user_id).by_not_corporate.not_false_positive.by_sev(AppConstants::SeverityTypes::CRITICAL).group_by_description
		end
		@severity_hash = init_severity_hash
		@severities_count.each { |sev_finds| @severity_hash[sev_finds.first] = sev_finds.last }
		calculate_sev_percentge 
	end
	def calculate_sev_percentge
		total = @severity_hash["Critical"] + @severity_hash["High"] + @severity_hash["Medium"] + @severity_hash["Low"] + @severity_hash["Info"]
		@critical_percentage,@high_percentage,@medium_percentage,@low_percentage,@info_percentage = 0,0,0,0,0
		unless total == 0
			@critical_percentage = (@severity_hash["Critical"].to_f/total.to_f)*100
			@high_percentage = (@severity_hash["High"]/total.to_f)*100
			@medium_percentage = (@severity_hash["Medium"].to_f/total.to_f)*100
			@low_percentage = (@severity_hash["Low"].to_f/total.to_f)*100
			@info_percentage = (@severity_hash["Info"].to_f/total.to_f)*100
		end
	end
	def get_desc_wise
		if session[:analytics_type] == AppConstants::OwnerTypes::CORPORATE
			@findings = Finding.includes(:repo,:scaners).by_description(params[:description]).not_false_positive.by_corporate.by_sev(AppConstants::SeverityTypes::CRITICAL).paginate(:page => params[:page])	
		else
			@findings = current_user.findings.includes(:repo,:scaners).by_description(params[:description]).not_false_positive.by_not_corporate.by_sev(AppConstants::SeverityTypes::CRITICAL).paginate(:page => params[:page])
		end
	end
end
