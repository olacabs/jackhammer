module ApplicationHelper
	def is_add_scan_enabled?
		get_roles
		current_user.is_admin? || current_user.is_team_lead? || (@functionalities.present? && @functionalities.include?("Scan") && @operations_list.include?("scan_create")) 
	end
	def m( string )
		string = string.to_s.strip
		return '' if string.empty?

		html = Kramdown::Document.new( string).to_html
		Loofah.fragment( html ).scrub!(:prune).to_s.html_safe
	end
	def current_class?(current_url)
		return 'active' if request.path == current_url
		''
	end
	def custom_bootstrap_flash
		flash_messages = []
		flash.each do |type, message|
			type = 'info' if type == 'notice'
			type = 'error'   if type == 'alert'
			text = "toastr.#{type}('#{message}');"
			flash_messages << text.html_safe if message
		end
		flash_messages.join("\n").html_safe
	end
	def generate_csv
		CSV.generate do |csv|
			csv << Finding.column_names
			if @findings.present?
				@findings.each do |find|
					csv << find.attributes.values_at(*Finding.column_names)
				end
			end
		end
	end
	def get_alert
		@user_notifications = AlertNotification.recent_alerts(current_user)
		@notification_count = @user_notifications.count
	end
	def can_manage_roles?
		current_user.is_admin? || current_user.is_security_member? || current_user.is_team_lead?
	end
	def font_awesome_icon(icon_name)
		"<i class='fa #{icon_name} pull-right' aria-hidden='true'></i>"
	end
	def bootstrap_icon(icon_name)
		"<i class='glyphicon #{icon_name} pull-right' aria-hidden='true'></i>"
	end
	def field_info(info)
		"<a href='#' data-toggle='popover' data-trigger='hover' width='500px;' font-size='8px;' data-content='#{info}'><i class='glyphicon glyphicon-info-sign text-info'></i></a>"
	end
	def  optional_text
		"<span class='text-muted'>(Optional)<span>"
	end
	def repo_results_icons(icon,severity,not_false=true)
		if not_false
			"<i class='fa #{icon} #{severity.downcase}' aria-hidden='true'></i> #{severity} <span class='#{severity}-row badge' id='not_false_#{severity.downcase}'>  #{@severity_hash[severity]}</span>".html_safe
		else
			"<i class='fa #{icon} #{severity.downcase}' aria-hidden='true'></i> #{severity} <span class='#{severity}-row badge' id='false_#{severity.downcase}'>  #{@false_severity_hash[severity]}</span>".html_safe
		end
	end
	def sidebar_icons(icon)
		"<span style='font-size:16px;' class='pull-right hidden-xs showopacity fa #{icon}'></span>"
	end
end
