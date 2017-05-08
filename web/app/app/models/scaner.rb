class Scaner < ActiveRecord::Base
	self.per_page = 10	
	belongs_to :user
	has_and_belongs_to_many :findings,dependent: :destroy
	belongs_to :team
	belongs_to :repo
	belongs_to :branch
	has_many :scaner_instances
	has_attached_file :project_target
	validates_attachment_content_type :project_target,content_type: ['application/zip','application/tar','text/xml','application/xml','application/octet-stream']

	scope :corporate_scans_by_status,->(status) { where("scaners.status = ? and team_id is not null",status).count}
	scope :personal_scans_by_status,->(status,user_id) { where("scaners.status = ? and user_id = ?",status,user_id).count}
	scope :corporate_scans,-> {where("team_id is not null")}
	scope :team_scans,->(team_ids) {where("team_id is not null and team_id in (?)",team_ids)}
	scope :personal_scans,->(user_id) {where("team_id is null and user_id = ?",user_id)}
	scope :corporate_scans_by_scan_type,->(scan_type) {where("team_id is not null and scan_type=? and is_upload_scan = false",scan_type)}
	scope :team_scans_by_scan_type,->(team_ids,scan_type) {where("team_id in (?) and scan_type=? and is_upload_scan = false",team_ids,scan_type)}
	scope :personal_scans_by_scan_type,->(user_id,scan_type) {where("team_id is null and user_id = ? and scan_type=?",user_id,scan_type)}
	scope :uploaded_corporate_scans,->{where("team_id is not null and is_upload_scan = true")}
	scope :uploaded_team_scans,->(team_ids) {where(team_id: team_ids,is_upload_scan: true)}
	def save_findings(findings,scaner_instance)
		findings.flatten.each do |result|
			if self.team_id.present?
				previous = Finding.by_repo_id(self.repo_id).by_teams(self.team_id).fingerprint(result.fingerprint).order("created_at").last
			else
				previous = Finding.by_repo_id(self.repo_id).by_user(self.user_id).fingerprint(result.fingerprint).order("created_at").last
			end
			if previous.present?
				unless self.findings.include?(previous)
					self.findings << previous 
					scaner_instance.findings << previous
					increment_scanner_sev_count(self,scaner_instance,previous)
				end
				previous.scaners << self unless previous.scaners.include?(self)
				next
			else
				if result.source[:scanner] == 'BundleAudit' || result.source[:scanner] == 'Dawnscanner'
					issue_type = "Ruby Gem"
				elsif result.source[:scanner] == 'Brakeman'
					issue_type = "Ruby Code"
				elsif result.source[:scanner] == 'FindSecurityBugs' || result.source[:scanner] == 'PMD' || result.source[:scanner] == 'Xanitizer'
					issue_type = "Java Code"
				elsif result.source[:scanner] == "NPM" || result.source[:scanner] == "RetireJS" || result.source[:scanner]=="NodeSecurityProject" 
					issue_type = "Java Script/Node Js"
				elsif result.source[:scanner] == "TruffleScanner"	
					issue_type = "Hardcode Secret Token"
				end
				severity = normalize_severity(result.severity)
				f = Finding.create({
					:description => result.description,
					:severity => severity,
					:fingerprint => result.fingerprint,
					:detail => result.detail,
					:scanner => result.source[:scanner],
					:file => result.source[:file],
					:line => result.source[:line],
					:code => result.source[:code],
					:external_link=> result.link,
					:solution=> result.solution,
					:cvss_score=> result.cvss_score,
					:location=> result.location,
					:user_input=> result.user_input,
					:advisory=> result.advisory,
					:repo_id=> self.repo_id,
					:team_id=> self.team_id,
					:scan_type=> self.scan_type,
					:owner_type=> self.owner_type,
					:user_id=> self.user_id,
					:scaner_instance_id=> scaner_instance.id,
					:scaner_id=> self.id,
					:issue_type=> issue_type
				})
				#Scaner.update_sev_count(f) if f.repo.team.present?
				if f.valid?
					increment_sev_count(f,self,scaner_instance)
					f.scaners << self unless f.scaners.include?(self)
					self.findings << f unless self.findings.include?(f)
				end
			end
		end 
	end
	def normalize_severity(severity)
		return "Low" if severity.strip.downcase == 'weak' || severity.strip.downcase == 'unknown'
		return "Info" if severity.strip.downcase == 'informational'
		return severity.capitalize
	end
	def increment_scanner_sev_count(scanner,scanner_instance,finding)
		scanner.total_count += 1
		case finding.severity
		when 'Critical'	
			scanner.critical_count += 1
		when 'High'
			scanner.high_count += 1
		when 'Medium'
			scanner.medium_count += 1
		when 'Low'
			scanner.low_count += 1
		when 'Info'
			scanner.info_count += 1	
		end
		scanner.save
	end
	def increment_sev_count(finding,scanner,scanner_instance)
		scanner.total_count += 1
		case finding.severity
		when 'Critical'
			scanner.critical_count += 1
			scanner_instance.critical_count += 1 if scanner_instance.present?
			finding.repo.critical_count +=  1 if finding.repo.present?
			finding.team.critical_count +=  1 if finding.team.present?
		when 'High'
			scanner.high_count += 1
			scanner_instance.high_count += 1  if scanner_instance.present?
			finding.repo.high_count += 1 if finding.repo.present?
			finding.team.high_count += 1 if finding.team.present?
		when 'Medium'
			scanner.medium_count += 1
			scanner_instance.medium_count += 1 if scanner_instance.present?
			finding.repo.medium_count += 1 if finding.repo.present?
			finding.team.medium_count +=  1  if finding.team.present?
		when 'Info'
			scanner.info_count += 1
			scanner_instance.info_count += 1 if scanner_instance.present?
			finding.repo.info_count += 1 if finding.repo.present?
			finding.team.info_count += 1 if finding.team.present?
		when 'Low'
			scanner.low_count += 1
			scanner_instance.low_count += 1 if scanner_instance.present?
			finding.repo.low_count += 1 if finding.repo.present?
			finding.team.low_count +=  1 if finding.team.present?
		end
		scanner.save
		if scanner_instance.present?
			scanner_instance.total_count += 1
			scanner_instance.save
		end
		if finding.repo.present?
			finding.repo.total_count += 1 
			finding.repo.save
		end
		if finding.team.present?
			finding.team.total_count += 1  
			finding.team.save 
		end
	end
	def send_notifications
		user_notification = self.user.notification
		mail_ids = self.user.email
		if Setting['notification'].present?
			if user_notification.present? && user_notification.critical_count.present? && self.critical_count > user_notification.critical_count
				mail_ids = mail_ids + "," + user_notification.critical_email
			end
			if user_notification.present? && user_notification.high_count.present? && self.high_count > user_notification.high_count
				mail_ids = mail_ids + "," + user_notification.high_email
			end
			if user_notification.present? && user_notification.medium_count.present? && self.medium_count > user_notification.medium_count
				mail_ids = mail_ids + "," + user_notification.medium_email
			end
			if user_notification.present? && user_notification.low_count.present? && self.low_count > user_notification.low_count
				mail_ids = mail_ids + "," + user_notification.low_email
			end
			if user_notification.present? && user_notification.info_count.present? && self.info_count > user_notification.info_count
				mail_ids = mail_ids + "," + user_notification.info_email
			end
			mail_ids = mail_ids.split(",").uniq.join(",")
			NotificationMailer.send_email(self,mail_ids,self.critical_count,self.high_count,self.medium_count,self.low_count,self.info_count).deliver
		end
	end
	def self.update_sev_count(finding)
		#finding = finding.includes(:scaners,:repo,:team)
		scaner = finding.scaners.first
		scaner_instance = finding.scaner_instance
		scaner.total_count = scaner.total_count - 1 if scaner.present?
		scaner_instance.total_count -= 1 if scaner_instance.present?
		finding.repo.total_count = finding.repo.total_count - 1 if finding.repo.present?
		finding.team.total_count = finding.team.total_count - 1 if finding.team.present?
		case finding.severity
		when 'Critical'
			scaner.critical_count = scaner.critical_count - 1
			scaner_instance.critical_count -= 1 if scaner_instance.present?
			finding.repo.critical_count = finding.repo.critical_count - 1 if finding.repo.present?
			finding.team.critical_count = finding.team.critical_count - 1 if finding.team.present?
		when 'High'
			scaner.high_count = scaner.high_count - 1
			scaner_instance.high_count -= 1  if scaner_instance.present?
			finding.repo.high_count = finding.repo.high_count - 1 if finding.repo.present?
			finding.team.high_count = finding.team.high_count - 1 if finding.team.present?
		when 'Medium'
			scaner.medium_count -= 1
			scaner_instance.medium_count -= 1  if scaner_instance.present?
			finding.repo.medium_count = scaner.repo.medium_count - 1 if finding.repo.present?
			finding.team.medium_count = scaner.team.medium_count - 1 if finding.team.present?
		when 'Info'
			scaner.info_count -= 1
			scaner_instance.info_count -= 1  if scaner_instance.present?
			finding.repo.info_count = scaner.repo.info_count - 1 if finding.repo.present?
			finding.team.info_count = scaner.team.info_count - 1 if finding.team.present?
		when 'Low'
			scaner.low_count -= 1
			scaner_instance.low_count -= 1  if scaner_instance.present?
			finding.repo.low_count = finding.repo.low_count - 1 if finding.repo.present?
			finding.team.low_count = finding.team.low_count - 1 if finding.team.present?
		end
		scaner.save
		scaner_instance.save
		finding.repo.save if finding.repo.present?
		finding.team.save if finding.team.present?
	end
	def update_scan_status(scan_status,status_reason)
		if scan_status == 'failed' || scan_status == 'done'
			update(status: scan_status,status_reason: status_reason,is_scanned: true)
		else
			update(status: scan_status,status_reason: status_reason)
		end
	end
	def get_findings_group_by_desc_and_link(severity)
		findings.where(severity: severity).group(:description,:external_link).order("total_count DESC").limit(5).pluck("count(*) as total_count,description,external_link")
	end
end
