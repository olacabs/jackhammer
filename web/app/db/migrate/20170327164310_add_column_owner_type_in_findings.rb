class AddColumnOwnerTypeInFindings < ActiveRecord::Migration
  def change
	  add_column :findings,:owner_type,:string
	  add_index :findings,:owner_type
  end
end
