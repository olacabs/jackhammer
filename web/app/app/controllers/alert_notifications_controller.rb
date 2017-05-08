class AlertNotificationsController < ApplicationController
	def index
		@notifications = current_user.alert_notifications
	end
	def update_alerts
		@notifications = current_user.alert_notifications.update_all(read:true)
		render :nothing => true, :status => 200, :content_type => 'text/html'
	end
	def link_through
		@notification = UserNotification.find(params[:id])
		@notification.update read: true
		redirect_to post_path @notification.post
	end

end
