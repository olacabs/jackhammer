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
		runsystem(true, "arachni", "#{@trigger.path}","--report-save-path", "#{report_file.path}")
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

