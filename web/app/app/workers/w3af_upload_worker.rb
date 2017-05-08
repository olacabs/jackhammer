class W3afUploadWorker 
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include UploadTemplateService
	sidekiq_options unique: :while_executing
	attr_accessor :logger, :template, :templates_dir,:plugin
	def perform(scan,file=nil)
		@scan = Scaner.find(scan)
		if file.present?
			file_content = File.read(file)
		else
			file_content = File.read(@scan.project_target.path)
		end
		@doc = Nokogiri::XML( file_content )
		if doc.search('vulnerability').empty?
			@scan.update_scan_status("failed","The document doesn't seem W3af XML format")
		else
			vulns = doc.search('vulnerability')
		end
	end
	def process_simple

	end
end
