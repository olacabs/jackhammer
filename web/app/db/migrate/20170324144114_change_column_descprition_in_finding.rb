class ChangeColumnDescpritionInFinding < ActiveRecord::Migration
  def change
	  change_column :findings,:description,:text
	#  add_index :findings,:description
  end
end
