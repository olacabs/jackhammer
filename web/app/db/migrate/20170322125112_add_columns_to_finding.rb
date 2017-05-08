class AddColumnsToFinding < ActiveRecord::Migration
  def change
    add_column :findings, :scan_type, :string
    add_column :findings, :team_id, :integer
  end
end
