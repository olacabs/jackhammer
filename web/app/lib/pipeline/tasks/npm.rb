require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'find'
require 'pry'

class Pipeline::Npm < Pipeline::BaseTask

	Pipeline::Tasks.add self
	include Pipeline::Util

	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = "NPM"
		@description = "Node Package Manager"
		@stage = :file
		@labels << "file" << "javascript"
		@results = []
	end

	def run
		exclude_dirs = ['node_modules','bower_components']
		exclude_dirs = exclude_dirs.concat(@tracker.options[:exclude_dirs]).uniq if @tracker.options[:exclude_dirs]
		directories_with?('package.json', exclude_dirs).each do |dir|
			Pipeline.notify "#{@name} scanning: #{dir}"
			Dir.chdir(dir) do
				if @tracker.options.has_key?(:npm_registry)
					registry = "--registry #{@tracker.options[:npm_registry]}"
				else
					registry = nil
				end
				@command = "npm install -q --ignore-scripts #{registry}"
				@results << runsystem(true, @command)
			end
		end
	end

	def analyze
		begin
			if @results.include? false
				Pipeline.warn 'Error installing javascript dependencies with #{@command}'
			end
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		end
	end

	def supported?
		supported = find_executable0('npm')
		unless supported
			Pipeline.notify "Install npm: https://nodejs.org/en/download/"
			return false
		else
			return true
		end
	end

end
