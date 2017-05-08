class AddColumnsToFindings < ActiveRecord::Migration
  def change
	  add_column :findings,:port,:string
	  add_column :findings,:protocol,:string
	  add_column :findings,:state,:string
	  add_column :findings,:product,:string
	  add_column :findings,:scripts,:binary
	  add_column :findings,:version,:string
	  add_column :findings,:host,:string
  end
end
