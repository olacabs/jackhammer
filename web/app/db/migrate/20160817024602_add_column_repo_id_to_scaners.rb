class AddColumnRepoIdToScaners < ActiveRecord::Migration
  def change
    add_column :scaners, :repo_id, :integer
    add_column :scaners,:branch_name,:string
  end
end
