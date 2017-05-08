require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'tempfile'

class Pipeline::TruffleScanner < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "TruffleScanner"
		@description = "Truffle Scanner static app security scanner"
		@stage = :code
		@labels << "code"
	end


	def run
		@results_file = Tempfile.new(['secret', 'xml'])
		regx_file = Dir.glob( File.join(@tracker.options[:truffle_hog_path],"regx_config_*")).select  {|f| File.file? f }.sort_by {|f| File.mtime f }.last
		#pipeline_settings = Setting.find_by(var: 'pipeline').value
		pipeline_settings = Setting.find_by(var: 'pipeline')
                if pipeline_settings.present?
			pipeline_settings = pipeline_settings.value
		else
			pipeline_settings = Setting.pipeline
                end
		if pipeline_settings['commits_depth'].present?
			if pipeline_settings['commit_start_date'].present? 
				runsystem(true,"python","#{@tracker.options[:truffle_hog_path]}/truffle.py","--path",@trigger.path,"--outfile",@results_file.path,"--branch","master","--regex","#{regx_file}","--depth","#{pipeline_settings['commits_depth']}","--start",pipeline_settings['commit_start_date']) 
			else
				runsystem(true,"python","#{@tracker.options[:truffle_hog_path]}/truffle.py","--path",@trigger.path,"--outfile",@results_file.path,"--branch","master","--regex","#{regx_file}","--depth","#{pipeline_settings['commits_depth']}")
			end

		else
			if pipeline_settings['commit_start_date'].present?
				runsystem(true,"python","#{@tracker.options[:truffle_hog_path]}/truffle.py","--path",@trigger.path,"--outfile",@results_file.path,"--branch","master","--regex","#{regx_file}","--start",pipeline_settings['commit_start_date'])

			else
				runsystem(true,"python","#{@tracker.options[:truffle_hog_path]}/truffle.py","--path",@trigger.path,"--outfile",@results_file.path,"--branch","master","--regex","#{regx_file}")
			end
		end
		@results = JSON.parse(File.read(@results_file.path))
	end

	def analyze
		if @results.present?
			@results.each do |each_find|
				begin 
					bug_type = 'Secret Token'
					sev = 'High'
					detail = each_find['diff'].map { |each_diff| each_diff.first['vulnerable'] }.join("\n")
					if each_find['diff'].kind_of?(Array)
						each_find['diff'].each do |sub_find|
							source = {:scanner => "TruffleScanner", :file => nil, :line => nil, :code => sub_find.first["vulnerable"]}
							fprint = fingerprint("#{bug_type}#{source}#{sev}")
							location = '<b>commit message:  </b>' + each_find['commit'] + '  <b>branch:  </b>' + each_find['branch'] + ' <b>date:  </b>' + each_find['date']
							link = ""
							report bug_type,detail, source,sev, fprint,link,location,'' 
						end
					else
						source = {:scanner => "TruffleScanner", :file => nil, :line => nil, :code => each_find['diff']}
						fprint = fingerprint("#{bug_type}#{detail}#{source}#{sev}")
						location = '<b>commit message:</b>' + each_find['commit'] + '  <b>branch: </b>' + each_find['branch'] + ' </b>date: </b>' + each_find['date']
						link = ""
						report bug_type,detail, source,sev, fprint,link,location,''
					end
				rescue Exception=>e
					Pipeline.notify "Error while Reading finding ===> #{e.backtrace}"
				end
			end
		end
	ensure
		FileUtils.rm @results_file

	end
	def supported?
		#	arachni_supported = runsystem(true, "arachni", "--version")
		#	arachni_reporter_supported = runsystem(true, "arachni_reporter", "--version")
		#	if arachni_supported =~ /command not found/
		#		Pipeline.notify "Install arachni: 'gem install arachni'"
		#		return false
		#	elsif arachni_reporter_supported =~ /command not found/
		#		Pipeline.notify "Install arachni-reactor: 'gem install arachni-reactor'"
		#	else
		return true
		#	end
	end

end

