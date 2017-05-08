class FindingsController < ApplicationController
	before_action :set_finding,only: [:change_status,:details,:change_false_positive,:change_to_not_exploitable]      
	before_action :setup_scan,only: [:index]
	include FindingsHelper
	def index
		if !page_can_access?(@scaner)
			unauthorized_page 
		else
			respond_to do |format|
				@invloved_tools = Array.new
				if @scaner.found_langs.present?
					@scaner.found_langs.split(",").each do |lang|
						@invloved_tools << Setting.pipeline['tasks_for'][lang].split(",") if Setting.pipeline['tasks_for'][lang].present?
					end     
				end
				@invloved_tools = @invloved_tools.flatten.uniq.compact
				current_scan_findings
				format.html
				format.json { render json: FindingsDatatable.new(view_context,@scaner,current_user) }
			end
		end
	end
	def current_scan_findings
		get_user_settings
		if @severity_levels.present? || @scaners_levels.present?
			if @severity_levels.present? && @scaners_levels.present?
				@findings = @scaner.findings.where("lower(severity) in (?) and scanner in (?) ",@severity_levels,@scaners_levels)
			elsif @severity_levels.present?
				@findings = @scaner.findings.where("lower(severity) in (?)",@severity_levels)
			elsif @scaners_levels.present?
				@findings = @scaner.findings.where("scanner in (?)",@scaners_levels)
			end
		else
			@findings = @scaner.findings
		end
	end
	def details
		unauthorized_page unless page_can_access?(@finding)
		@scan_id = params[:scan_id]
		current_user_roles
		get_comments_and_uploads
		#get_prev_and_next
	end

	def change_status
		@finding.update(status: params['status'],closed_by: current_user.name)	   
		if params['status'] == 'Close'
			@finding.update(closed_date: Date.today.to_date)
		end
		logger.info "changing finding status to #{params['status']} for the finding #{@finding.id}"
		respond_to do |format|
			@msg="Finding Status Changed Successfully"
			format.js
		end             
	rescue => e             
		respond_to do |format|
			@msg= e.message
			format.js
		end

	end
	def change_false_positive
		@finding.toggle(:is_false_positive)
		logger.info "marking as false positive for the finding #{@finding.id}"
		Scaner.update_sev_count(@finding) if @finding.repo.present? && @finding.repo.team.present?
		@finding.closed_by = current_user.name
		@finding.save
		respond_to do |format|
			flash[:notice]="False positive Status Changed Successfully"
			format.js
		end
	rescue => e
		respond_to do |format|
			flash[:notice] = e.message
			format.js
		end             
	end
	def change_to_not_exploitable
		@finding.toggle(:not_exploitable)
		logger.info "marking as not exploitable for the finding #{@finding.id}"
		Scaner.update_sev_count(@finding) if @finding.repo.present? && @finding.repo.team.present?
		@finding.closed_by = current_user.name
		@finding.save
		respond_to do |format|
			flash[:notice]="Change to not exploitable Successfully"
			format.js
		end
	rescue => e
		respond_to do |format|
			flash[:notice]= e.message
			format.js
		end
	end
	def publish_to_jira
		begin
			@finding = Finding.find(params[:finding_id])
			jira_details = Setting['jira']
			crypt = ActiveSupport::MessageEncryptor.new(Rails.application.secrets.secret_key_base)
			unless jira_details.present?
				logger.error "Jira detials are not configured...."
				raise 'Jira detials are not configured' 
			end
			project = @finding.try(:repo).try(:name)
			jira_options = {
				:site => jira_details['host'],
				:username => jira_details['username'],
				:password => crypt.decrypt_and_verify(jira_details['password']),
				:context_path => '' ,#Setting.jira['context_path'],
				:auth_type => :basic,
			}
			jira = JIRA::Client.new(jira_options)
			jira_project = ""
			if project.present?
				jira_project = jira.Project.all.select { |p| p.name.downcase == project.downcase}.try(:first)
			else
				jira_project = jira.Project.all.select { |p| p.name.downcase == jira_details["default_project"].downcase}.try(:first)
			end
			jira_project = jira.Project.all.select { |p| p.name.downcase == jira_details["default_project"].downcase}.try(:first) unless jira_project.present?
			if jira_project.present?
				project_key = jira_project.key
			else
				raise 'Project Not found in jira'
			end
			issue = jira.Issue.build
			result = issue.save({
				"fields" => {
					"project" => {
						"key" => project_key
					},
					"issuetype" => {
						"name" => "Bug"
					},
					"summary" => "#{project} - #{@finding.description}",
					"description" => get_jira_body(@finding),
						"labels" => [ "security-issue", project.gsub(" ","-") ]
				}
			})
			@finding.toggle(:is_publish_to_jira)
			@finding.update(jira_key: issue.key)
			respond_to do |format|
				logger.info "finding with id  #{@finding} is published to jira"
				flash[:notice] = "issue published Successfully"
				format.js   
			end
		rescue => e
			respond_to do |format|
				flash[:error] = "Jira details are not confgured"
				format.js
			end
		end
	end
	def delete_file
		@upload = Upload.find(params[:upload_id])
		@upload.destroy
		respond_to do |format|
			@msg="File deleted successfully.."
			format.js
		end
	rescue => e
		respond_to do |format|
			@msg= e.message
			format.js
		end
	end
	def set_finding
		@finding = Finding.find(params[:finding_id])
	end
	def next_finding
		current_find = Finding.find(params[:finding_id].to_i)
		@current_scan = current_find.scaners.first
		if params[:pagination] == 'Prev'
			@finding = @current_scan.findings.where("id < ? ",params[:finding_id].to_i).last
		else
			@finding = @current_scan.findings.where("id > ? ",params[:finding_id].to_i).first
		end
		get_comments_and_uploads
		render "details"
	end
	def get_comments_and_uploads
		@comments = @finding.comments
		@uploads = @finding.uploads
		@tags = @finding.tags.map(&:name).join(",")
		@is_false_positive_present = is_flase_positive_enabled?
		@is_change_status_present = is_change_status_enabled?
		@is_tagging_present = is_tagging_enabled?
		@is_comments_present = is_comments_enabled?
		@is_upload_files_present = is_upload_files_enabled?
		@is_upload_delete_enabled = is_upload_delete_enabled?
		@col_span = get_col_span
	end
	def save_tag
		@tag = Tag.find_or_create_by(name: params[:tag_name])
		@tag.update(user_id: current_user.id)
		finding = Finding.find(params[:finding_id])
		if params[:remove_tag] == "true"
			flash[:notice] = "Removed tagging"
			finding.tags.delete(@tag)
		else
			flash[:notice] = "tagging is done"
			finding.tags << @tag
		end
		@tags_count = finding.tags.count
		respond_to do |format|
			format.js
		end
	end
	def update
		finding = Finding.find(params[:id])
		finding.severity = params[:finding][:severity]
		if finding.save
			render :json => {"severity" => finding.severity}
		else
			render :json => finding.errors.full_messages, :status => :unprocessable_entity
		end
	end
	private 

	def is_flase_positive_enabled?
		can_access_all? ||  (@functionalities.present? && @functionalities.include?("Mark False Positive"))
	end

	def is_tagging_enabled?
		can_access_all? ||  (@functionalities.present? && @functionalities.include?("Tagging"))
	end
	def is_comments_enabled?
		can_access_all? ||  (@functionalities.present? && @functionalities.include?("Comments")) 
	end
	def is_upload_files_enabled?
		can_access_all? ||  (@functionalities.present? && @functionalities.include?("Upload Files"))
	end
	def is_change_status_enabled?
		can_access_all? ||  (@functionalities.present? && @functionalities.include?("Change Vulnerable Status"))
	end
	def is_upload_delete_enabled?
		can_access_all? ||  (@functionalities.present? && @functionalities.include?("upload_delete"))
	end
	def can_access_all?
		current_user.is_admin? || Setting.application_mode == AppConstants::UserMode::SINGLE_USER || current_user.is_security_member? || current_user.is_team_lead?
	end
	def get_col_span
		is_features_enabled = any_feature_enabled? 
		if is_features_enabled
			return 8
		else
			return	12
		end
	end
	def get_prev_and_next
		@is_prev_present = false
		@is_next_present = false
		@scan = Scaner.where(id: @finding.scaner_id).try(:first)
		#@scan = @finding.scaners.try(:first)
		@prev = @scan.try(:findings).where("id < ? ",@finding.id) if params[:page] == "list_findings"
		@next = @scan.findings.where("id > ? ",@finding.id) if params[:page] == "list_findings"
		@is_prev_present = true if @prev.try(:first).present?
		@is_next_present = true if @next.try(:first).present?
	end
	def any_feature_enabled?
		current_user.is_admin? || current_user.is_security_member? || current_user.is_team_lead? || (@functionalities.present? && @functionalities.include?("Tagging")) || (@functionalities.present? && @functionalities.include?("Comments")) || (@functionalities.present? && @functionalities.include?("Upload Files"))
	end

	def setup_scan
		session[:scan_id] = params[:scan_id] 
		@scaner = Scaner.find(session[:scan_id])
	end
end
