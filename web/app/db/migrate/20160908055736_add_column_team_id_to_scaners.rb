class AddColumnTeamIdToScaners < ActiveRecord::Migration
  def change
     add_column :scaners,:team_id,:integer
  end
end
