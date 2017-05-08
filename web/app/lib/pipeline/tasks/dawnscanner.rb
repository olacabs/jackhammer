require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'tempfile'

class Pipeline::DawnScanner < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "DawnScanner"
		@description = "DawnScanner ruby analyzer"
		@stage = :code
		@labels << "code"
	end

	def run
		Dir.chdir("#{@trigger.path}") do
			@results_file = Tempfile.new(['dawnresults', 'xml'])
			runsystem(true, "dawn", "-F", "#{@results_file.path}", "-j", ".")
			@results = JSON.parse(File.read("#{@results_file.path}"))['vulnerabilities']
		end
	end

	def analyze
		begin
			@results.each do |result|
				description = result['name'].gsub('\n',' ')
				link = result['cve_link']
				solution = result['remediation']
				detail = result['message']
				cvss_score = result['cvss_score']
				source = {:scanner => @name, :file => nil, :line => nil, :code => nil}
				sev = result['severity'].capitalize
				fprint = fingerprint("#{description}#{detail}#{source}#{sev}")

				report description, detail, source, sev, fprint,link,'','','',solution,cvss_score
			end
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		ensure
			File.unlink @results_file
		end
	end
	def supported?
		supported=runsystem(true, "dawn", "--version")
		if supported =~ /command not found/
			Pipeline.notify "Install dawnscanner: 'gem install dawnscanner'"
			return false
		else
			return true
		end
	end

end

