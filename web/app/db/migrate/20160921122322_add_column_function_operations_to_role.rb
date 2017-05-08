class AddColumnFunctionOperationsToRole < ActiveRecord::Migration
  def change
	  add_column :roles,:function_operations,:string
  end
end
