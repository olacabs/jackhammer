class AddColumnToTeam < ActiveRecord::Migration
  def change
    add_column :teams, :is_default_team, :boolean
    add_column :scaners,:parameters,:string
  end
end
