class ChnageColumnRequestAndResponseInFinding < ActiveRecord::Migration
  def change
	  change_column :findings,:request,:text
	  change_column :findings,:response,:text
  end
end
