class AddColumnStatusReasonToScaner < ActiveRecord::Migration
  def change
      add_column :scaners,:status_reason,:string
  end
end
