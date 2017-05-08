class AddColumnRepoTypeToRepo < ActiveRecord::Migration
  def change
    add_column :repos, :repo_type, :string
  end
end
