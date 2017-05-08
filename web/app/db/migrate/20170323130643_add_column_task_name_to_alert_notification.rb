class AddColumnTaskNameToAlertNotification < ActiveRecord::Migration
  def change
    add_column :alert_notifications, :task_name, :string
  end
end
