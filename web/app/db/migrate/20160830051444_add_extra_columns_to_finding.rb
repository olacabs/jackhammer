class AddExtraColumnsToFinding < ActiveRecord::Migration
  def change
      add_column :findings,:external_link,:string
      add_column :findings,:solution,:string
      add_column :findings,:cvss_score,:string
      add_column :findings,:location,:string
      add_column :findings,:user_input,:string
      add_column :scaners,:no_of_processed_files,:integer
      add_column :scaners,:directories,:integer
      add_column :scaners,:start_time,:time
      add_column :scaners,:end_time,:time
  end
end
