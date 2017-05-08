class Finding < ActiveRecord::Base
	has_and_belongs_to_many :scaners,dependent: :destroy
	belongs_to :repo
	belongs_to :branch
	belongs_to :team
	belongs_to :user
	has_many :comments,  dependent: :destroy
	has_many :uploads	       
	belongs_to :scaner_instance
	has_many :taggings
	has_many :tags, through: :taggings
	scope :by_repo_id,-> (repo_id){ where('findings.repo_id = ?', repo_id) }
	scope :by_team_id,->(team_id){ where('findings.team_id = ?', team_id) }
	scope :branch,              -> (branch)       { where("findings.branch_id LIKE ?", branch ||= "%") }
	scope :fingerprint,         -> (fingerprint)  { where("findings.fingerprint LIKE ?", fingerprint ||= "%") }
	scope :filters_severities_count,->(where_clause) {where(where_clause).group(:severity).pluck("severity,count(*)")}
	scope :filter_finds,->(where_clause) {where(where_clause).uniq}
	scope :by_tools,->(enabled_tools) {where("scanner in (?)",enabled_tools)}
	scope :by_sev,->(enabled_severities) {where("severity in (?)",enabled_severities)}
	scope :by_corporate,-> {where("findings.team_id is not null")}
	scope :by_not_corporate,-> {where("findings.team_id is null")}
	scope :not_false_positive,-> {where("findings.is_false_positive = false and status!='Close'")}
	scope :by_false_positive,->{where("findings.is_false_positive = true")} 
	scope :by_status,->(status) {where("findings.status in(?)",status)}
	scope :by_teams,->(team_ids) {where("findings.team_id in (?)",team_ids)}
	scope :by_scan_type,->(scan_type){where("findings.scan_type in(?)",scan_type)}
	scope :by_description,->(description){where("findings.description in(?)",description)}
	scope :repo_not_blank,->{where("findings.repo_id is not null")}
	scope :top_vul_repos,->(limit){group("repo_id").order("count(*) DESC").limit(limit).pluck("repo_id,count(*) as total_count")}
	scope :by_user,->(user_id){where("user_id = ?",user_id)}
	scope :by_owner,->(owner_type){where("owner_type in(?)",owner_type)}
	scope :group_by_description,-> { group(:description).order("count(*) DESC").limit(5).pluck("description,count(*) as total_count")}
	scope :group_by_repo_count,->{group("repo_id").pluck("repo_id,count(*)")}
	scope :group_by_month,->{group("year(created_at)").group("month(created_at)").group("severity").count}
	scope :group_by_severity,->{group("severity").pluck("severity,count(*) as total_count")}
	scope :group_by_team_sev_count,->{group("team_id,severity").pluck("team_id,severity,count(*) as total_count")}
	scope :group_by_repo_sev_count,->{group("repo_id,severity").pluck("repo_id,severity,count(*) as total_count")}
	def self.update_finding(findings,current_user,commit)
		findings.each do |find|
			finding = find(find)
			finding.closed_by = current_user.name
			if commit.include?("Exploitable")
				finding.toggle!(:not_exploitable)
				logger.info "marking as not Exploitable for the finding #{finding.id}"
			else
				finding.toggle!(:is_false_positive)
				logger.info "marking as false positive/mark valid for the finding #{finding.id}"
				#Scaner.update_sev_count(finding) if finding.is_false_positive == false
			end
		end
	end
	def self.marked_false_positive?
		self.is_false_positive
	end
end
