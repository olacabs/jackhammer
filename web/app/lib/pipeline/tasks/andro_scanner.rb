require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'json'
class Pipeline::AndroScanner < Pipeline::BaseTask
	Pipeline::Tasks.add self
	include Pipeline::Util
	def initialize(trigger, tracker)
		super(trigger, tracker)
		@name = 'AndroScanner' 
		@description = "AndroScan will scan APK files and provide with a list of vulnerabilities and potential secrets hardcoded in the APK" 
		@stage = :code
		@labels << "code" << "ruby" << "rails"
	end
	def run
		apk_file = Dir.entries(@trigger.path)
		apk_file.delete(".")
		apk_file.delete("..")
		apk_path = "#{@trigger.path}/#{apk_file.first}"
		@results_file = "#{Rails.root}/tools/androscan/#{SecureRandom.hex}.json"
		Dir.chdir "#{Rails.root}/tools/androscan"
		runsystem(true,"python" ,"#{Rails.root}/tools/androscan/mandrobugs.py","-e","2","-f","#{apk_path}","-j","#{@results_file}") 
		@results = JSON.parse(File.read("#{@results_file}"))
	end
	def analyze
		begin
			if @results.present?
				@results["sensitive"].each do |issue_key|

					bug_type = "Sensitive data"	#mapping bug-type
					description = "These are the sensitive strings hardcoded in the APK" #mapping issue description

					location = "" #mapping location
					line = "" #mapping line no
					file = ""  #mapping file 
					code = issue_key  #mapping code 
					source = {:scanner => @name, :file => file, :line => line, :code => code}

					sev = "Info"  #mapping severity 
					user_input = ""
					advisory = ""
					solution = "Review all the hardcoded values in the APK and remove the sensitive one from the APK"
					cvss_score = ""

					fprint = fingerprint("#{bug_type}#{description}#{source}") #generating fingerprint
					#pass the value as empty string if any value is not present
					report bug_type,description,source,sev,fprint,"","",user_input,advisory,solution,cvss_score

				end

				@results["details"].each do |issue_key,issue_value|

					bug_type = issue_key	#mapping bug-type
					description = issue_value["title"]  #mapping issue description
					location = "" #mapping location
					line = "" #mapping line no
					file = ""  #mapping file 
					code = issue_value["vector_details"]  #mapping code 
					source = {:scanner => @name, :file => file, :line => line, :code => code}

					issue_value['level'] = 'High' if issue_value['level'].downcase == 'warning'

					issue_value['level'] = 'Low' if issue_value['level'].downcase == 'notice'

					sev = issue_value['level']  #mapping severity 
					user_input = issue_value['summary']
					advisory = issue_value['cve_number']
					solution = ""
					cvss_score = ""

					fprint = fingerprint("#{bug_type}#{description}#{source}") #generating fingerprint
					#pass the value as empty string if any value is not present
					report bug_type,description,source,sev,fprint,"","",user_input,advisory,solution,cvss_score
				end
			end
		rescue Exception => e
			Pipeline.warn e.message
			Pipeline.warn e.backtrace
		end


	ensure
		FileUtils.rm @results_file 

	end
	def supported?
		#unless File.exist?("#{Rails.root}/tools/androscan/AndroBugs_Framework/__init__.py")
		#   Pipeline.notify "Run: runThisFirst.py"
		#runsystem(true, "python #{Rails.root}/tools/androscan/runThisFirst.py")
		#  return false
		#else
		return true
		#end
	end
end
