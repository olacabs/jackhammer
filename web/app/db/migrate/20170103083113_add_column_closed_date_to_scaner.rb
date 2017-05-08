class AddColumnClosedDateToScaner < ActiveRecord::Migration
  def change
    add_column :findings, :closed_date, :date
  end
end
