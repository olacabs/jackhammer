class AddCountColumnsToTeam < ActiveRecord::Migration
  def change
	  add_column :teams,:total_count,:integer,:default=>0
	  add_column :teams,:critical_count,:integer,:default=>0
	  add_column :teams,:high_count,:integer,:default=>0
	  add_column :teams,:medium_count,:integer,:default=>0
	  add_column :teams,:low_count,:integer,:default=>0
	  add_column :teams,:info_count,:integer,:default=>0
	  add_column :repos,:total_count,:integer,:default=>0
	  add_column :repos,:critical_count,:integer,:default=>0
	  add_column :repos,:high_count,:integer,:default=>0
	  add_column :repos,:medium_count,:integer,:default=>0
	  add_column :repos,:low_count,:integer,:default=>0
	  add_column :repos,:info_count,:integer,:default=>0
  end
end
