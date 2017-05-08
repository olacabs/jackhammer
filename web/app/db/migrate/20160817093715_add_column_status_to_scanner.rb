class AddColumnStatusToScanner < ActiveRecord::Migration
  def change
      add_column :scaners,:status,:string
  end
end
