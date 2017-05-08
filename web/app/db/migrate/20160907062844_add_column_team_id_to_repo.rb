class AddColumnTeamIdToRepo < ActiveRecord::Migration
  def change
     add_column :repos,:team_id,:integer
  end
end
