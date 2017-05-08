require 'nmap/xml'
class NmapUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	attr_accessor :logger, :template, :templates_dir,:plugin
	sidekiq_options unique: :while_executing
	def perform(scan,file=nil)
		#file_content    = File.read(scaner_params[:project_target].tempfile)
		@scan = Scaner.find(scan)
		 if file.present?
                        file_name = file
                else
                        file_name = @scan.project_target.path
                end 
		doc = Nmap::XML::new(file_name)
		if doc.hosts.empty?
			@scan.update_scan_status("failed","Invalid file format")
		else
			@total_count,@critical_count,@high_count,@medium_count,@low_count,@info_count = 0,0,0,0,0,0
			doc.each_host do |host|
				host_label = host.ip
				host.each_port do |port|
					save_finding(bug_type: port.try('service').try('name'),severity: 'High',scanner: 'Nmap',detail: port.reason,port: port.number,protocol: port.protocol.to_s,product: port.try('service').try('product'),scripts: port.try('scripts'),version:  port.try('service').try('version'),host: host.ip)
				end
			end
			update_sev_count
		end

	end
end
