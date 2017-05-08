class AddColumnScanIdToAlertNotification < ActiveRecord::Migration
  def change
    add_column :alert_notifications, :scaner_id, :integer
  end
end
