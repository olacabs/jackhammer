class CreateJoinTableTeamUser < ActiveRecord::Migration
  def change
    remove_column :users,:team_id
    create_join_table :teams, :users do |t|
      # t.index [:team_id, :user_id]
      # t.index [:user_id, :team_id]
    end
  end
end
