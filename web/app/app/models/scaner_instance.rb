class ScanerInstance < ActiveRecord::Base
	belongs_to :scaner
	has_many :findings,dependent: :destroy
	def get_latest_findings_group_by_desc_and_link(severity)
		findings.where(severity: severity).group(:description,:external_link).order("total_count DESC").limit(5).pluck("count(*) as total_count,description,external_link")
	end
end
