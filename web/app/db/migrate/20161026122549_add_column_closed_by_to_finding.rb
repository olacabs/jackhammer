class AddColumnClosedByToFinding < ActiveRecord::Migration
  def change
	  add_column :findings,:closed_by,:string
  end
end
