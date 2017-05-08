class AddColumnTeamIdToUser < ActiveRecord::Migration
  def change
      add_column :users,:team_id,:integer
      add_column :users,:is_team_lead?,:boolean,:default=>false
  end
end
