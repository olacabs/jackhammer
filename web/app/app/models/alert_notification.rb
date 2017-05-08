class AlertNotification < ActiveRecord::Base
	belongs_to :user
	def self.recent_alerts(user)
		user.alert_notifications.where(read:false).first(5)
	end
end
