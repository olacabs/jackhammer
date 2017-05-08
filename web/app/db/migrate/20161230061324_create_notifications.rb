class CreateNotifications < ActiveRecord::Migration
  def change
    create_table :notifications do |t|
      t.integer :user_id
      t.integer :critical_count
      t.integer :high_count
      t.integer :medium_count
      t.integer :low_count
      t.string :critical_email
      t.string :medium_email
      t.string :low_email
      t.string :info_email
      t.integer :info_count
      t.string :high_email

      t.timestamps null: false
    end
  end
end
