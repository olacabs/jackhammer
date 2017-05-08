class RenameDataTypesInFindings < ActiveRecord::Migration
  def change
	  change_column :findings,:description,:binary
	  change_column :findings,:detail,:binary
	  change_column :findings,:solution,:binary
	  change_column :findings,:code,:binary
  end
end
