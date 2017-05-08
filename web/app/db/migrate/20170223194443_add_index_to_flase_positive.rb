class AddIndexToFlasePositive < ActiveRecord::Migration
  def change
	  add_index :findings,:is_false_positive
  end
end
