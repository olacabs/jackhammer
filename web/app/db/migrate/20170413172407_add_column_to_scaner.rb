class AddColumnToScaner < ActiveRecord::Migration
  def change
    add_column :scaners, :is_upload_scan, :boolean,:default=>false
  end
end
