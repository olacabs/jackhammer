class AddColumnIssuTypeToFinding < ActiveRecord::Migration
  def change
	  add_column :findings,:issue_type,:string
  end
end
