class ChnageDefaultVlaueForTotalCount < ActiveRecord::Migration
  def change
	  change_column :scaners,:total_count,:integer,:default=>0
  end
end
