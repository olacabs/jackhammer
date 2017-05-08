class AddIndexesToAllTables < ActiveRecord::Migration
	def change
		add_index :scaner_instances,:total_count
		add_index :scaner_instances,:critical_count
		add_index :scaner_instances,:high_count
		add_index :scaner_instances,:medium_count
		add_index :scaner_instances,:low_count
		add_index :scaner_instances,:info_count
		add_index :scaner_instances,:scaner_id
		add_index :repos,:team_id
		add_index :repos,:total_count
		add_index :repos,:critical_count
		add_index :repos,:high_count
		add_index :repos,:medium_count
		add_index :repos,:low_count
		add_index :repos,:info_count
		add_index :scaners,:total_count
		add_index :scaners,:critical_count
		add_index :scaners,:medium_count
		add_index :scaners,:high_count
		add_index :scaners,:low_count
		add_index :scaners,:info_count
		add_index :teams,:total_count
		add_index :teams,:critical_count
		add_index :teams,:high_count
		add_index :teams,:medium_count
		add_index :teams,:low_count
		add_index :teams,:info_count
	end
end
