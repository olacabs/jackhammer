class AddColumnPeriodicScheduleToScaner < ActiveRecord::Migration
  def change
	  add_column :scaners,:periodic_schedule,:string
	  add_column :scaners,:last_run,:date
  end
end
