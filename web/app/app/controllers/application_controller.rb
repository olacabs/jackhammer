class ApplicationController < ActionController::Base
	# Prevent CSRF attacks by raising an exception.
	# For APIs, you may want to use :null_session instead.
	protect_from_forgery with: :exception
	#before_action :configure_action_mailer, if: :devise_controller?
	before_action :authenticate_user!
	def after_sign_in_path_for(resource)
		if Setting.application_mode == AppConstants::UserMode::SINGLE_USER
			display_dashboards_path(dashboard_type: 'personal',scan_type: 'Static')
		else
			display_dashboards_path(dashboard_type: 'corporate',scan_type: 'Static')
		end
	end
	def after_sign_out_path_for(resource)
		response.headers["Cache-Control"] = "no-cache, no-store, max-age=0, must-revalidate"
		response.headers["Pragma"] = "no-cache"
		response.headers["Expires"] = "Fri, 01 Jan 1990 00:00:00 GMT"
		root_path
	end
	def fetch_findings_severity_wise(findings)
		severity_hash = init_severity_hash
		if findings.present?
			findings.each do |find|
				severity_hash[find.severity]+=1 if severity_hash.include?(find.severity)
			end
		end
		severity_hash
	end
	def get_user_settings
		@scaners_levels = Setting.pipeline["enabled_tools"].present? ? Setting.pipeline["enabled_tools"].split(",") : ""
		@severity_levels = current_user.tasks.where(group: 'Severity').map(&:name).uniq.map(&:camelcase)
		if @scaners_levels.present?
			@scaners_levels  = AppConstants::TOOLS::DEFAULT_EXTERNAL_TOOLS + @scaners_levels 
		else
			@scaners_levels  = AppConstants::TOOLS::DEFAULT_EXTERNAL_TOOLS
		end
	end
	def get_findings_count(scan)
		findings_count = 0
		["Critical","High","Medium","Low","Info"].each do |sev|
			sev_column = sev.downcase + "_count"
			findings_count += scan.send(sev_column).to_i if @severity_levels.include?(sev.downcase)
		end
		findings_count
	end
	def month_wise_findings(findings)
		findings.each do |instance|
			previous_month = (instance.created_at - 1.month).strftime("%b") +  "-" + instance.created_at.strftime("%y")
			if @month_hash.include?(previous_month)
				@month_hash[instance.created_at.strftime("%b") + "-" + instance.created_at.strftime("%y") ][instance.severity] = @month_hash[instance.created_at.strftime("%b") + "-" + instance.created_at.strftime("%y") ][instance.severity] + @month_hash[(instance.created_at - 1.month).strftime("%b") + "-" + instance.created_at.strftime("%y") ][instance.severity] + 1
			else
				@month_hash[instance.created_at.strftime("%b") + "-" + instance.created_at.strftime("%y") ][instance.severity]+=1 
			end
		end
	end
	def get_findings_array
		@critical_count = @month_hash.map { |m,s| s["Critical"] }
		@high_count = @month_hash.map { |m,s| s["High"] }
		@medium_count = @month_hash.map { |m,s| s["Medium"] }
		@low_count = @month_hash.map { |m,s| s["Low"] }
		@info_count = @month_hash.map { |m,s| s["Info"] }

	end
	def init_severity_hash
		{"Critical"=>0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0}
	end
	def top_vul_repos
		@most_vul_repos = Hash.new
		@scans.each do |scan|
			if scan.repo.present?
				findings = scan.repo.findings.where.not(is_false_positive: true)
				findings = findings.where("scanner in (?)",@scaners_levels) if @scaners_levels.present?
				findings = findings.where("lower(severity) in (?)",@severity_levels) if @severity_levels.present?
				@most_vul_repos[scan.repo.name] = findings.count
			end
		end
		@most_vul_repos = @most_vul_repos.sort_by { |k,v| v }.reverse.first(5)
	end
	def get_last_one_years_moths
		month_count = 0
		last_one_year_months = Hash.new
		iteration_count = 0
		while(iteration_count < 13 )
			month = Date.today - month_count 
			last_one_year_months[month.strftime("%b") + "-" + month.strftime("%y")] = init_severity_hash 
			month_count = month_count + 30
			iteration_count = iteration_count + 1
		end
		last_one_year_months = Hash[last_one_year_months.to_a.reverse]
	end
	def fetch_scan_findings(scan)
		findings = scan.findings.where.not(is_false_positive: true)
		findings = findings.where("scanner in (?)",@scaners_levels) if @scaners_levels.present?
		findings = findings.where("lower(severity) in (?)",@severity_levels) if @severity_levels.present?
		findings.order(:created_at)
	end
	def unauthorized_page
		render 'errors/unauthorized', :status => :unauthorized
	end
	rescue_from (StandardError) do |exception|
		if exception.class == CanCan::AccessDenied
			unauthorized_page
		else
			logger.error "Some exception occured...#{exception.inspect} "
			handle_exception
		end
	end
	def handle_exception(exception=nil)
		return render_500
	end
	def render_500
		respond_to do |format|
			format.html { render template: 'errors/internal_server_error', layout: 'layouts/application', status: 500 }
			format.all { render nothing: true, status: 500}
		end
	end
	def update_settings
		results = {}    
		params[:settings].each do |setting, value|
			if params[:settings]["pipeline"].present? && params[:settings]["pipeline"]["truffle_regx_1"].present?
				config_file = File.join(Rails.root,"tools","truffleHog","regx_config_#{SecureRandom.hex(8)}.txt")
				regx_file = File.open(config_file,"w")
				(1..5).each do |each_regx|
					regx_file.puts params[:settings]["pipeline"]["truffle_regx_" + each_regx.to_s] if params[:settings]["pipeline"]["truffle_regx_" + each_regx.to_s].present?
				end
				regx_file.close
			end
			if value.include?("password") || value.include?("api_access_token")
				crypt = ActiveSupport::MessageEncryptor.new(Rails.application.secrets.secret_key_base)
				encrypted_data = crypt.encrypt_and_sign(params[:settings][setting]["password"])
				params[:settings][setting]["password"] = encrypted_data
				encrypted_data = crypt.encrypt_and_sign(params[:settings][setting]["api_access_token"])
				params[:settings][setting]["api_access_token"] = encrypted_data
			end
			Setting.merge!(setting.to_sym, value)
			configure_action_mailer if params[:settings][:notification].present?
			results.merge!({setting.to_sym => value})
		end   
	end    
	def severity_levels
		["Critical","High","Medium","Low","Info"]	
	end
	def copy_prev_month_stats
		severity_levels.each do |s|
			@month_hash[@month_hash.keys.last(2).last][s] = @month_hash[@month_hash.keys.last(2).last][s] + @month_hash[@month_hash.keys.last(2).first][s] if @month_hash[@month_hash.keys.last(2).last][s] == 0
		end
	end
	def current_user_roles
		roles = current_user.roles
		if roles.present?
			@functionalities = roles.map { |role| role.functionalities }.flatten
			@functionalities = @functionalities.map(&:name)
			@operations_list = roles.map { |role| role.function_operations.split(",") if role.function_operations.present? }.flatten
		end
	end
	def configure_action_mailer
		begin
			Devise.setup do |config| 
				config.mailer_sender = Setting["notification"]["email"] 
			end
			@host = Setting["notification"]["host"]
			@host = Setting["notification"]["host"][0...-1] if  Setting["notification"]["host"][-1] == '/'
			ActionMailer::Base.default_url_options = {:host => @host}
			crypt = ActiveSupport::MessageEncryptor.new(Rails.application.secrets.secret_key_base)
			ActionMailer::Base.smtp_settings = { address: Setting["notification"]["address"],port: Setting["notification"]["port"].to_i, domain: Setting["notification"]["domain"], enable_starttls_auto: true,authentication: 'plain',user_name: Setting["notification"]["email"] ,password: crypt.decrypt_and_verify(Setting["notification"]["password"])}	
		rescue Exception=> exception
			logger.error "configuration mailer error..#{exception.inspect} "		 
		end
	end
	def get_roles
		roles = current_user.roles
		if roles.present?
			@functionalities = roles.map { |role| role.functionalities }.flatten
			@functionalities = @functionalities.map(&:name)
			@operations_list = roles.map { |role| role.function_operations.split(",") if role.function_operations.present? }.flatten
		end
	end
	def page_can_access?(obj)
		return (current_user.is_admin? || current_user.is_security_member? || current_user.is_team_lead?) if obj.owner_type == 'corporate'
		return (obj.user == current_user)
	end
end
