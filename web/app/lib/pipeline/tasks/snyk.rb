require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'redcarpet'

class Pipeline::Snyk < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "Snyk"
		@description = "Snyk.io JS dependency checker"
		@stage = :code
		@labels << "code" << "javascript"
		@results = []
	end

	def run
		exclude_dirs = ['node_modules','bower_components']
		exclude_dirs = exclude_dirs.concat(@tracker.options[:exclude_dirs]).uniq if @tracker.options[:exclude_dirs]
		directories_with?('package.json', exclude_dirs).each do |dir|
			Pipeline.notify "#{@name} scanning: #{dir}"
			Dir.chdir(dir) do
				@results << JSON.parse(runsystem(true, "snyk", "test", "--json"))["vulnerabilities"]
			end
		end
	end

	def analyze
		begin
			markdown = Redcarpet::Markdown.new Redcarpet::Render::HTML.new(link_attributes: {target: "_blank"}), autolink: true, tables: true
			@results.each do |dir_result|
				# We build a single finding for each uniq result ID, adding the unique info (upgrade path and files) as a list
				dir_result.uniq {|r| r['id']}.each do |result|
					description = result['title']

					# Use Redcarpet to render the Markdown details to something pretty for web display
					detail = markdown.render(result['description']).gsub('h2>','strong>').gsub('h3>', 'strong>')
					upgrade_paths = [ "Upgrade Path:\n" ]
					files = []

					# Pull the list of files and upgrade paths from all results matching this ID
					# This uses the same form as the retirejs task so it all looks nice together
					dir_result.select{|r| r['id'] == result['id']}.each do |res|
						res['upgradePath'].each_with_index do |upgrade, i|
							upgrade_paths << "#{res['from'][i]} -> #{upgrade}"
						end
						files << res['from'].join('->')
					end

					source = {
						:scanner => @name,
						:file => files.join('<br>'),
						:line => nil,
						:code => upgrade_paths.uniq.join("\n"),
					}
					sev = result['severity'].capitalize
					fprint = fingerprint("#{description}#{detail}#{source}#{sev}")
					cvss_score = result['identifiers']['CWE'] 
					advisory = result['patches'].first['urls'].first	
					report description, detail, source, sev, fprint,'','','',advisory,'',cvss_score
				end
			end
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		end
	end

	def supported?
		supported = find_executable0('snyk')
		unless supported
			Pipeline.notify "Install Snyk: 'npm install -g snyk'"
			return false
		else
			return true
		end
	end
end

