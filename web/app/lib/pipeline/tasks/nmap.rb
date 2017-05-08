require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'json'
#Change class name same as "Scannername" added from the settings page, class name should be in camel case without space  
class Pipeline::Nmap < Pipeline::BaseTask
	Pipeline::Tasks.add self
	include Pipeline::Util
	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = 'Nmap' #Update "Scannername" as provided from settings page 
		@description = "This is a description of the tool name" #ex: Describe your scanner (Optional)
		@stage = :code
		@labels << "code" << "ruby" << "rails"
	end
	def run
		# Replace "scannerreport" with a report name (optional)
		# Replace "Fileextension" with json or xml based on the tool's output compatibility 
		@results_file = Tempfile.new(['nmap', 'xml'])
		target = URI.parse(@trigger.path).try(:host)
		target = @trigger.path unless target
		user_input = Scaner.find(@trigger.scan_id).parameters
		#Copy the full command from terminal Ex: (nmap 10.20.200.1 -oX text.xml) and separte each word within a double quote separated by a comma ex: "nmap",@trigger.path,"-oX". Ignore the file name ex: "text.xml"
		if user_input.present?
			runsystem(true, "nmap","-s#{user_input}","#{target}","-oX","#{@results_file.path}") 
		else
			runsystem(true, "nmap","#{target}","-oX","#{@results_file.path}")
		end
		#@results = JSON.parse(File.read("#{@results_file.path}"))
	end
	def analyze

		begin
			NmapUploadWorker.new.perform(@trigger.scan_id,@results_file.path)
			#NmapUploadWorker.perform_async(@trigger.scan_id,@results_file.path)
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		end
	ensure
		#FileUtils.rm @results_file #file gets deleted after parsing is done
	end
	# Check , if the scanner is installed
	def supported?
		supported=runsystem(true, "nmap", "--version")
		if supported =~ /command not found/
			Pipeline.notify "Install nmap: 'brew install nmap'"
			return false
		else
			return true
		end
	end
end
