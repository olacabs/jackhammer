require 'pipeline/tasks/base_task'
require 'json'
require 'pipeline/util'
require 'digest'

class Pipeline::BundleAudit < Pipeline::BaseTask
	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "BundleAudit"
		@description = "Dependency Checker analysis for Ruby"
		@stage = :code
		@labels << "code" << "ruby"
		@results = {}
	end

	def run
		directories_with?('Gemfile.lock').each do |dir|
			Pipeline.notify "#{@name} scanning: #{dir}"
			Dir.chdir(dir) do
				@results[dir] = runsystem(true, "bundle-audit", "check")
			end
		end
	end

	def analyze
		begin
			get_warnings
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.notify "Appears not to be a project with Gemfile.lock or there was another problem ... bundle-audit skipped."
		end
	end

	def supported?
		supported=runsystem(false, "bundle-audit", "update")
		if supported =~ /command not found/
			Pipeline.notify "Run: gem install bundler-audit"
			return false
		else
			return true
		end
	end


	private
	def get_warnings
		@results.each do |dir, result|
			detail, jem, source, sev, hash,link,solution,advisory = '','',{},'','','','',''
			result.each_line do | line |

				if /\S/ !~ line
					# Signal section is over.  Reset variables and report.
					if detail != ''
						report "Gem #{jem} has known security issues.", detail, source, sev, fingerprint(hash),link,'','',advisory,solution
					end

					detail, jem, source, sev, hash,link,solution,advisory = '','', {},'','','','',''
				end

			name, value = line.chomp.split(':')
			case name
			when 'Name'
				jem << value
				hash << value
			when 'Version'
				jem << value
				hash << value
			when 'Advisory'
				source = { :scanner => @name, :file => "#{relative_path(dir, @trigger.path)}/Gemfile.lock", :line => nil, :code => nil }
				advisory = value
				hash << value
			when 'Criticality'
				sev = value.strip.camelcase
				hash << sev
			when 'URL'
			         link = line.chomp.split('URL:').last
			when 'Title'
				detail = value
			when 'Solution'
				solution = value
			when 'Insecure Source URI found'
				report "Insecure GEM Source", "#{line.chomp} - use git or https", {:scanner => @name, :file => 'Gemfile.lock', :line => nil, :code =>  nil},'high', fingerprint("bundlerauditgemsource#{line.chomp}")
			else
				if line =~ /\S/ and line !~ /Unpatched versions found/
					Pipeline.debug "Not sure how to handle line: #{line}"
				end
			end
			end
		end
	end


end


