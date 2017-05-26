require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'tempfile'

class Pipeline::Arachni < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "Arachni"
		@description = "Arachni Scanner web app security scanner"
		@stage = :code
		@labels << "code"
	end


	def run
		report_file = Tempfile.new(['arachni','afr'])
		@result_xml_path = Tempfile.new(['arachni','xml'])
		scaner = Scaner.find(@trigger.scan_id)
		sign_check = "Sign Off|MY ACCOUNT|Logout|Sign out|Sign Out|Welcome|Hello|Profile|Logoff|Log out|LogOut|Log Out|Signoff|Sign off"
		logout_check = "logout|Signout|Signoff|Sign out|Sign off|Sign Out|Sign Off|Log out|Log Out"
		is_authenticated_scan = scaner.web_login_url.present? && scaner.username_param.present? && scaner.username_param_val.present? && scaner.password_param.present? &&  scaner.password_param_val.present?
		if  is_authenticated_scan
			runsystem(true,"arachni","#{@trigger.path}","--report-save-path","#{report_file.path}","--plugin=autologin:url=#{scaner.web_login_url},parameters=#{scaner.username_param}=#{scaner.username_param_val}&#{scaner.password_param}=#{AESCrypt.decrypt(scaner.password_param_val,Rails.application.secrets.secret_key_base)},check=#{sign_check}","--scope-exclude-pattern=#{logout_check}")
		else
			runsystem(true, "arachni", "#{@trigger.path}","--report-save-path", "#{report_file.path}")
		end
		runsystem(true, "arachni_reporter", "#{report_file.path}","--reporter=xml:outfile=#{@result_xml_path.path}")
	end

	def analyze
		ArachniUploadWorker.new.perform(@trigger.scan_id,@result_xml_path.path)
		#ArachniUploadWorker.perform_async(@trigger.scan_id,@result_xml_path.path)
	end
	def supported?
		arachni_supported = runsystem(true, "arachni", "--version")
		arachni_reporter_supported = runsystem(true, "arachni_reporter", "--version")
		if arachni_supported =~ /command not found/
			Pipeline.notify "Install arachni: 'gem install arachni'"
			return false
		elsif arachni_reporter_supported =~ /command not found/
			Pipeline.notify "Install arachni-reactor: 'gem install arachni-reactor'"
		else
			return true
		end
	end

end

