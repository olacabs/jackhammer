class AddColumnBranchIdToScaner < ActiveRecord::Migration
  def change
      add_column :scaners,:branch_id,:integer
  end
end
