class ChnageColumnIsActive < ActiveRecord::Migration
  def change
	  change_column :tasks,:is_active?,:boolean,:default=> true
  end
end
