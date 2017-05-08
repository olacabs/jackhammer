class NotificationMailer < ApplicationMailer
	default css: 'email'
	def send_email(scan,email_ids,critical,high,medium,low,info)
		@scan = scan
		@critical_c = @scan.findings.by_sev("Critical").count
		@high_c = @scan.findings.by_sev("High").count
		@medium_c = @scan.findings.by_sev("Medium").count
		@low_c = @scan.findings.by_sev("Low").count
		@info_c = @scan.findings.by_sev("Info").count
		@target = @scan.target.present? ? @scan.target : @scan.repo.ssh_repo_url
		@repo_name = @scan.repo.present? ? @scan.repo.name  : @scan.project_title
		@critical_top_vul =  @scan.get_findings_group_by_desc_and_link("Critical")
		@high_top_vul  =  @scan.get_findings_group_by_desc_and_link("High")
		@medium_top_vul =  @scan.get_findings_group_by_desc_and_link("Medium")
		@low_top_vul = @scan.get_findings_group_by_desc_and_link("Low")
		@info_top_vul = @scan.get_findings_group_by_desc_and_link("Info")
		@scan_instances = @scan.scaner_instances
		@scan_instance = @scan.scaner_instances.last
		@latest_critical_top_vul  = @scan_instance.get_latest_findings_group_by_desc_and_link("Critical")
		@latest_high_top_vul  = @scan_instance.get_latest_findings_group_by_desc_and_link("High")
		@latest_medium_top_vul  = @scan_instance.get_latest_findings_group_by_desc_and_link("Medium")
		@latest_low_top_vul = @scan_instance.get_latest_findings_group_by_desc_and_link("Low")
		@latest_info_top_vul = @scan_instance.get_latest_findings_group_by_desc_and_link("Info")
		if Setting['notification'].present?
			set_mail_settings
			@port = Setting["notification"]["host"].split(":").last if Setting["notification"].present? && Setting["notification"]["host"].include?(":")
			mail(from: Setting["notification"]["email"],to: email_ids, subject: "Jackhammer Scan Results: #{@repo_name}" )
		else
			AlertNotification.create(user_id: @scan.user_id,identifier: 'envelope',alert_type: 'danger',message: "SMTP settings are not done")
		end
	end
	def set_mail_settings
		@host = Setting["notification"]["host"]
		@host = Setting["notification"]["host"][0...-1] if  Setting["notification"]["host"][-1] == '/'
		@port = Setting["notification"]["host"].split(":").last if Setting["notification"].present? && Setting["notification"]["host"].include?(":")
		ActionMailer::Base.default_url_options = {:host => @host,port: @port}
		crypt = ActiveSupport::MessageEncryptor.new(Rails.application.secrets.secret_key_base)
		ActionMailer::Base.smtp_settings = { address: Setting["notification"]["address"],port: Setting["notification"]["port"].to_i, domain: Setting["notification"]["domain"], enable_starttls_auto: true,authentication: 'plain',user_name: Setting["notification"]["email"] ,password: crypt.decrypt_and_verify(Setting["notification"]["password"])} 
	end
end
