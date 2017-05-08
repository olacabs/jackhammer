require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'nokogiri'
require 'tempfile'
require 'mkmf'

MakeMakefile::Logging.instance_variable_set(:@logfile, File::NULL)

class Pipeline::Xanitizer < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "Xanitizer"
		@description = "Xanitizer plugin for Java"
		@stage = :code
		@labels << "code"
	end

	def run
		@results_file = "#{@tracker.options[:xanitizer_path]}/#{SecureRandom.hex}.xml"
		runsystem(true,"#{@tracker.options[:xanitizer_path]}/XanitizerHeadless","rootDirectory=#{@trigger.path}","configFileDirectory=#{@trigger.path}","findingsListReportOutputFile=#{@results_file}","XanitizerLogLevel=OFF","createSnapshot=false")
		@results = Crack::XML.parse(File.read(@results_file))
	end

	def analyze
		begin
			if @results.present? && @results["XanitizerFindingsList"].present?
				findings = @results["XanitizerFindingsList"]["finding"]
				findings.each do |result|
					bug_type = result["problemType"]
					description = result["description"]
					detail = result["category"] 
					if result['class'].present?
						location = "Class: #{result['class']}"	 
					else
						location = ""
					end
					line = result["line"]
					file = result["file"]
					code = result["package"]
					source = {:scanner => @name, :file => file, :line => line, :code => code}
					sev = result['rating'].to_i
					link = ""
					case sev
					when 0..1.9
						severity = 'Info'
					when 2..3.9
						severity = 'Low'
					when 4..4.9
						severity = 'Medium'
					when 5..7.9
						severity = 'High'
					when 8..10
						severity = 'Critical'
					else
						severity = 'Unknown'
					end
					fprint = fingerprint("#{description}#{detail}#{source}")
					report bug_type,description, source,severity, fprint,link,location,''
				end
			end
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		end
	ensure
		#File.unlink @results_file
		FileUtils.rm @results_file
	end

	def supported?
		if @tracker.options.has_key?(:xanitizer_path) and File.exist?("#{@tracker.options[:xanitizer_path]}/XanitizerHeadless")
			return true
		else
			Pipeline.notify "Xanitizer support requires XanitizerHeadless executable file"
			Pipeline.notify "Please download xanitizer tool and configure it"
			return false
		end

	end

end

