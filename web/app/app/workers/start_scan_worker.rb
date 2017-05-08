class StartScanWorker
	include Sidekiq::Worker
	include Sidekiq::Status::Worker
	include Base
	sidekiq_options({ :queue => :default,:retry => true })
	def perform
		scans = Scaner.where("team_id is not null and  (status='Queued' or status='Scanning')").order("created_at DESC")
		unless scans.present?
			scans = Scaner.where("team_id is not null").order("created_at DESC")
		end
		scans.each do |scan|
			begin
				scaner_instance = scan.scaner_instances.build
				scaner_instance.save
				if scan.source != 'gitlab' && scan.source != 'github'
					klass = (scan.source.camelize + 'ScanWorker').constantize
					Timeout::timeout(2.hours) { klass.new.perform(scan.id,scaner_instance.id) }
				else
					Timeout::timeout(2.hours) { GitScanWorker.new.perform(scan.id,scaner_instance.id) }
				end
			rescue Exception=>e

			end
		end
	end
end
