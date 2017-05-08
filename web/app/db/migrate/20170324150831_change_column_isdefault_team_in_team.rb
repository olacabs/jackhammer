class ChangeColumnIsdefaultTeamInTeam < ActiveRecord::Migration
  def change
	  change_column :teams,:is_default_team,:boolean,:default=>false
  end
end
