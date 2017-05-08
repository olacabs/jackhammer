class AddColumnScanTypeToTasks < ActiveRecord::Migration
  def change
	  add_column :tasks,:scan_type,:string
  end
end
