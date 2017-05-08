class AddColumnIsCustomToTask < ActiveRecord::Migration
  def change
	  add_column :tasks,:is_custom,:boolean,:default=>false
	  add_column :tasks,:can_upgrade,:boolean,:default=>false
	  add_column :tasks,:is_direct_upgrade,:boolean,:default=>false
  end
end
