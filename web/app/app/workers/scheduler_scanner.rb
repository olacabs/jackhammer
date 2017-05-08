require 'sidekiq-scheduler'
class SchedulerScanner
	include Sidekiq::Worker
	def perform
		@scans = Scaner.where.not(periodic_schedule: "")
		@scans.each do |scan|
			if scan.last_run.present?
				is_scan_need_to_run = false
				is_scan_need_to_run = true if scan.periodic_schedule == "Daily" && scan.last_run.to_date + 1 == Date.today.to_date
				is_scan_need_to_run = true if scan.periodic_schedule == "Weekly" && scan.last_run.to_date + 7 == Date.today.to_date
				is_scan_need_to_run = true if scan.periodic_schedule == "Monthly" && scan.last_run.to_date + 30 == Date.today.to_date
				if is_scan_need_to_run
					scaner_instance = ScanerInstance.create(scaner_id: scan.id)
					start_scan(scan,scaner_instance)
				end
			end
		end
	end
	def start_scan(scan,scaner_instance)
		klass = (scan.source.camelize + 'ScanWorker').constantize
		klass.new.perform_async(scan.id,scaner_instance.id)
	end
end

