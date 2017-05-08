require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'nokogiri'
require 'tempfile'
require 'mkmf'

MakeMakefile::Logging.instance_variable_set(:@logfile, File::NULL)

class Pipeline::FindSecurityBugs < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "FindSecurityBugs"
		@description = "FindSecurityBugs plugin for FindBugs"
		@stage = :code
		@labels << "code"
	end

	def run
		@results_file = Tempfile.new(['findsecbugs','xml'])

		unless File.exist?("#{@trigger.path}/.git/config")
			Dir.chdir(@trigger.path) do
				runsystem(true, "git", "init")
				runsystem(true, "git", "add", "*")
				runsystem(true, "git", "commit", "-am", "fake commit for mvn compile")
			end
		end

		directories_with?('pom.xml').each do |dir| 
			Dir.chdir(dir) do
				runsystem(true, "mvn", "compile", "-fn")
			end
		end 

		Dir.chdir(@tracker.options[:findsecbugs_path]) do
			runsystem(true, "/bin/sh", "#{@tracker.options[:findsecbugs_path]}/findsecbugs.sh", "-effort:max", "-quiet", "-xml:withMessages", "-output", "#{@results_file.path}", "#{@trigger.path}")
			@results = Nokogiri::XML(File.read(@results_file)).xpath '//BugInstance'
		end
	end

	def analyze
		begin
			@results.each do |result|
				description = result.xpath('LongMessage').text
				bug_type = result.attributes['type'].value
				link = "https://find-sec-bugs.github.io/bugs.htm##{bug_type}"
				location = "Class: #{result.at_xpath('Method').attributes['classname'].value}, Method: #{result.at_xpath('Method').attributes['name'].value}"
				detail = "Class: #{result.at_xpath('Method').attributes['classname'].value}, Method: #{result.at_xpath('Method').attributes['name'].value}\n#{result.xpath('LongMessage').text}\nhttps://find-sec-bugs.github.io/bugs.htm##{bug_type}"
				file = result.at_xpath('SourceLine').attributes['sourcepath'].value
				trigger_path = Pathname.new(@trigger.path)
				real_path = nil
				trigger_path.find {|path| real_path = path if path.fnmatch "*/#{file}"}
				file = real_path.relative_path_from(trigger_path).to_s unless real_path.nil?

				line = result.at_xpath('SourceLine[@primary="true"]').attributes['start'].value
				code = "#{result.at_xpath('String').attributes['value'].value}"
				source = {:scanner => @name, :file => file, :line => line, :code => code}
				sev = result.attributes['priority'].value.to_i
				case sev
				when 1
					severity = 'Critical'
				when 2
					severity = 'High'
				when 3
					severity = 'Medium'
				when 4
					severity = 'Low'
				when 5
					severity = 'Info'
				else
					severity = 'Unknown'
				end
				fprint = fingerprint("#{description}#{detail}#{source}")

				report bug_type,description, source,severity, fprint,link,location,''
			end
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		ensure
			File.unlink @results_file
		end
	end

	def supported?
		unless find_executable0('mvn') and File.exist?("#{@trigger.path}/pom.xml")
			Pipeline.notify "FindSecurityBugs support requires maven and pom.xml"
			Pipeline.notify "Please install maven somewhere in your PATH and include a valid pom.xml in the project root"
			return false
		end

		unless @tracker.options.has_key?(:findsecbugs_path) and File.exist?("#{@tracker.options[:findsecbugs_path]}/findsecbugs.sh")
			Pipeline.notify "#{@tracker.options[:findsecbugs_path]}"
			Pipeline.notify "Download and unpack the latest findsecbugs-cli release: https://github.com/find-sec-bugs/find-sec-bugs/releases"
			return false
		else
			return true
		end
	end

end

