class MetasploitUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	attr_accessor :logger, :template, :templates_dir,:plugin
	sidekiq_options unique: :while_executing
	def perform(scan,file=nil)
		#file_content    = File.read(scaner_params[:project_target].tempfile)
		@scan = Scaner.find(scan)
		if file.present?
			file_content = File.read(file)
		else 
			file_content = File.read(@scan.project_target.path)
		end
		@doc = Nokogiri::XML(file_content)
		@total_count,@critical_count,@high_count,@medium_count,@low_count,@info_count = 0,0,0,0,0,0
		case @doc.root.name
		when 'MetasploitV5'
			# version_importer = Dradis::Plugins::Metasploit::Importers::Version5.new(@doc)
		when /MetasploitV/
			@scan.update_scan_status("failed","Invalid Metasploit version. Sorry, the XML file corresponds to a version of Metasploit")
		else
			@scan.update_scan_status("failed","Invalid XML file. The XML document didn't contain a Metasploit root tag. Did you upload a Metasploit XML file?")
		end
		parse_file


	end
	private
	def parse_file
		@doc.root.xpath('hosts/host').each do |xml_host|
			parse_host(xml_host)
		end
		update_sev_count
	end
	def parse_host(xml_host)
		xml_host.xpath('services/service').each do |xml_service|
			port     = xml_service.at_xpath('port').text
			protocol = xml_service.at_xpath('proto').text
			state    = xml_service.at_xpath('state').text
			save_finding(bug_type: xml_service.at_xpath('name').text,severity: 'High',scanner: 'Metasploit',detail: xml_service.at_xpath('info').text,port: port,protocol: protocol.to_s,state: state.to_s)
		end
	end
end
