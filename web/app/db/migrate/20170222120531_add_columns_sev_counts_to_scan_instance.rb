class AddColumnsSevCountsToScanInstance < ActiveRecord::Migration
  def change
	  add_column :scaner_instances,:total_count,:integer,:default=>0
	  add_column :scaner_instances,:critical_count,:integer,:default=>0
	  add_column :scaner_instances,:high_count,:integer,:default=>0
	  add_column :scaner_instances,:medium_count,:integer,:default=>0
	  add_column :scaner_instances,:low_count,:integer,:default=>0
	  add_column :scaner_instances,:info_count,:integer,:default=>0

  end
end
