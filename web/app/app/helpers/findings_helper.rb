module FindingsHelper
	def get_severity(severity)
		if severity == '0' 
			severity = 'Very Low' 
		elsif severity == '1' 
			severity = 'Low' 
		elsif severity == '2'
			severity = 'Medium' 
		elsif severity == '3'
			severity = 'High' 
		end 
		severity
	end
	def extract_url(input_string)
		formated_string = input_string.split(/\s+/).find_all { |u| u =~ /^https?:/ }  
		if formated_string.present?
			formated_string.first
		else
			nil
		end
	end
	def get_jira_body(finding)
		description = ""
		application_name = finding.scaners.first.project_title.present? ? finding.scaners.first.project_title : finding.scaners.first.repo.name
		description += "Application Name:\n".html_safe
		description += application_name +  "\n"
		description += "======================================\n"
		repo_url = finding.repo.ssh_repo_url 
		if repo_url.present?
			description += "Repo / Url:\n"
			description += repo_url +  "\n"
		end
		if finding.scanner == 'Namp'
			description += "Service:\n"
		else
			description += "Vulnerability Type:\n"
		end
		description += finding.description.html_safe.to_s +  "\n"
		description += "======================================\n"
		description += "Severity:\n"
		description +=finding.severity +  "\n"
		description += "======================================\n"
		description += "Detail/Description:\n"
		description += finding.detail.html_safe.to_s +  "\n"
		get_find_columns.each do |column|
			if finding.send(column).present?
				description += "======================================\n"
				description += column.to_s.camelcase + ":\n"
				description += finding.send(column) + "\n"
			end
		end
		description += "======================================\n"
		description += "Jackhammer URL:\n"
		if request.env["HTTP_HOST"].present?
			description += request.env["HTTP_HOST"] + "/findings/details?finding_id=#{finding.id}"  
		end
		description
	end
	def get_find_columns
		[:solution,:location,:user_input,:file,:line,:code,:advisory,:cvss_score,:external_link,:fingerprint,:host,:protocol,:state,:port,:product,:version,:scripts,:request,:response,:confidence]
	end
end
