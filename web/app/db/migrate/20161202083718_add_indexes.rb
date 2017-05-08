class AddIndexes < ActiveRecord::Migration
  def change
	  add_index :findings,:repo_id
	  add_index :findings,:severity
	  add_index :findings,:scanner
  end
end
