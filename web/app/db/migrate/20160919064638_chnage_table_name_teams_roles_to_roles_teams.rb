class ChnageTableNameTeamsRolesToRolesTeams < ActiveRecord::Migration
  def change
	  rename_table :teams_roles,:roles_teams
  end
end
