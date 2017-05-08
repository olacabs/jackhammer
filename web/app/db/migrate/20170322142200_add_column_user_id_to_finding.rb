class AddColumnUserIdToFinding < ActiveRecord::Migration
  def change
    add_column :findings, :user_id, :integer
    add_column :findings,:tag_id,:integer
    add_index :findings,:user_id
    add_index :findings,:team_id
    add_index :findings,:scan_type
    add_index :findings,:scaner_id
    add_index :findings,:created_at
    add_index :findings,:scaner_instance_id
    add_index :findings,:tag_id
  end
end
