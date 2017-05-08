module ScanersHelper
	def self.github
		Octokit::Client.new(:access_token => current_user.token)
	end
	def self.get_head_commit repo_url, branch
		branch ||= 'master'
		self.github.commits(strip_github_path(repo_url), branch).first.sha
	end
	def self.strip_github_path repo_url
		uri = URI.parse(repo_url)
		return uri.path.gsub(/^\//,'').gsub(/\/$/,'').gsub('.git','')
	end
	def finding_status_color(finding)
		status_color = ""
		if finding.status == 'Close'
			status_color = '#d9534f'
		elsif finding.status == 'New'
			status_color = '#5cb85c'
		elsif finding.status == 'Open'
			status_color = '#337ab7'
		elsif finding.status == 'Deferred'
			status_color = '#777777'
		elsif finding.status == 'Fix in progress'
			status_color = '#f0ad4e'
		end
		status_color
	end
	def is_personal_scan_active?
		return "" if current_user.is_security_member?
		return "active"
	end
	def get_owner_type                              
		if current_user.is_admin?       
			return "corporate"      
		elsif current_user.is_team_lead?        
			return 'team'                           
		else                                            
			return 'personal'
		end                                     
	end                                     
	def get_myscans_class                   
		return "" if current_user.is_admin?  || current_user.is_security_member?
		return "active"                                 
	end                                                     
	def get_corporate_class                                 
		return "active" if (current_user.is_admin? || current_user.is_security_member?) && params[:owner_type] != AppConstants::OwnerTypes::PERSONAL 
		return ""                       
	end        
end
