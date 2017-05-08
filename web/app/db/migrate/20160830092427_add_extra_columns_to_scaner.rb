class AddExtraColumnsToScaner < ActiveRecord::Migration
	def change
		add_column :scaners,:found_langs,:string
		add_column :scaners,:invloved_tools,:string
		change_column :scaners,:start_time,:string
		change_column :scaners,:end_time,:string
	end
end
