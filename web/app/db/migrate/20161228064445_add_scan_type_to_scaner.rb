class AddScanTypeToScaner < ActiveRecord::Migration
  def change
    add_column :scaners, :scan_type, :string
    add_column :scaners, :vulnerable_types, :string
    add_column :scaners, :total_count, :integer
    add_column :scaners, :critical_count, :integer
    add_column :scaners, :high_count, :integer
    add_column :scaners, :medium_count, :integer
    add_column :scaners, :low_count, :integer
    add_column :scaners, :info_count, :integer
  end
end
