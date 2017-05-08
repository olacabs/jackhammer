class AddColumnSourceToScaners < ActiveRecord::Migration
  def change
    add_column :scaners,:source,:string
  end
end
