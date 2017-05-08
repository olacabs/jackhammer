require 'pipeline/tasks/base_task'
require 'json'
require 'pipeline/util'
require 'pathname'

class Pipeline::Brakeman < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "Brakeman"
		@description = "Source analysis for Ruby"
		@stage = :code
		@labels << "code" << "ruby" << "rails"
	end

	def run
		rootpath = @trigger.path
		@result=runsystem(true, "brakeman", "-A", "-q", "-f", "json", "#{rootpath}")
	end

	def analyze
		begin
			parsed = JSON.parse(@result)
			if parsed.present?
				parsed["warnings"].each do |warning|
					file = relative_path(warning['file'], @trigger.path)
					link = warning['link']
					detail = warning['message']
					location = warning['location']
					user_input = warning['user_input']
					if ! warning['line']
						warning['line'] = "0"
					end
					if ! warning['code']
						warning['code'] = ""
					end
					source = { :scanner => @name, :file => file, :line => warning['line'], :code => warning['code'].lstrip }
					warning["confidence"] = "Low" if warning["confidence"].downcase == 'weak'
					report warning["warning_type"], detail, source,warning["confidence"].capitalize, fingerprint("#{warning['message']}#{warning['link']}#{severity(warning["confidence"])}#{source}"),link,location,user_input
				end
			end
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		end
	end
	def supported?
		supported=runsystem(true, "brakeman", "-v")
		if supported =~ /command not found/
			Pipeline.notify "Run: gem install brakeman"
			return false
		else
			return true
		end
	end

end

