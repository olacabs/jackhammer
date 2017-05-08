class CreateUserNotifications < ActiveRecord::Migration
  def change
    create_table :alert_notifications do |t|
      t.references :user, index: true
      t.string :identifier
      t.string :alert_type
      t.boolean :read,:default=>false
      t.string :message
      t.timestamps null: false
    end
  end
end
