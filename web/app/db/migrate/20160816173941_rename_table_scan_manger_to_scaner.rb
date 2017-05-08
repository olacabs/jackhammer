class RenameTableScanMangerToScaner < ActiveRecord::Migration
  def change
          rename_table :scan_managers, :scaners
  end
end
