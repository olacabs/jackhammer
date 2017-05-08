class AddColumnIsScannedToScaner < ActiveRecord::Migration
  def change
    add_column :scaners, :is_scanned, :boolean,:default=>false
  end
end
