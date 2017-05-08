class AddRenameColumnsInScaner < ActiveRecord::Migration
	def change
		change_column :scaners,:critical_count,:integer,:default=>0
		change_column :scaners,:medium_count,:integer,:default=>0
		change_column :scaners,:high_count,:integer,:default=>0
		change_column :scaners,:low_count,:integer,:default=>0
		change_column :scaners,:info_count,:integer,:default=>0
	end
end
