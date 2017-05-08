require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'nokogiri'
require 'pathname'

class Pipeline::PMD < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "PMD"
		@description = "PMD Source Code Analyzer"
		@stage = :code
		@labels << "code"
	end

	def run
		@tracker.options[:pmd_checks] ||= "java-basic,java-sunsecure"
		Dir.chdir @tracker.options[:pmd_path] do
			@results = Nokogiri::XML(runsystem(true,'bin/run.sh', 'pmd', '-d', "#{@trigger.path}", '-f', 'xml', '-R', "#{@tracker.options[:pmd_checks]}")).xpath('//file')
		end
	end

	def analyze
		begin
			@results.each do |result|
				attributes = result.at_xpath('violation').attributes
				warning_type = attributes['rule']
				solution = result.children.children.to_s.strip
				detail = "Ruleset: #{attributes['ruleset']}"
				link = attributes['externalInfoUrl']
				source = {:scanner => @name, :file => result.attributes['name'].to_s.split(Pathname.new(@trigger.path).cleanpath.to_s)[1][1..-1], :line => attributes['beginline'].to_s, :code => "package: #{attributes['package'].to_s}\nclass: #{attributes['class'].to_s}\nmethod: #{attributes['method'].to_s}" }
				case attributes['priority'].value.to_i
				when 5
					sev = 1
					severity = 'Info'
				when 4
					sev = 2
					severity = 'Low'
				when 3
					sev = 3
					severity = 'Medium'
				when 2
					sev = 4
					severity = 'High'
				when 1
					sev = 5
					severity = 'Critical'
				else
					sev = 0
					severity = 'Weak'
				end
				fprint = fingerprint("#{description}#{detail}#{source}#{sev}")

				report warning_type,detail,source, severity, fprint,link,'','','',solution
			end
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		end
	end
	def supported?
		unless @tracker.options.has_key?(:pmd_path) and File.exist?("#{@tracker.options[:pmd_path]}/bin/run.sh")
			Pipeline.notify "#{@tracker.options[:pmd_path]}"
			Pipeline.notify "Install PMD from: https://pmd.github.io/"
			return false
		else
			return true
		end
	end

end
