class AddColumnSolutionToFinding < ActiveRecord::Migration
  def change
      add_column :findings,:advisory,:string
  end
end
