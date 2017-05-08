class ChnageColumnsInFindingAddColumToUser < ActiveRecord::Migration
  def change
	  change_column :findings,:detail,:text
	  change_column :findings,:file,:text
	  change_column :findings,:code,:text
	  change_column :findings,:external_link,:text
	  change_column :findings,:solution,:text
	  change_column :findings,:scripts,:text
	  add_column :users,:is_security?,:boolean
  end
end
