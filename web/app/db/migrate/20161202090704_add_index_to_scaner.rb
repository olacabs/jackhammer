class AddIndexToScaner < ActiveRecord::Migration
  def change
	  add_index :scaners,:user_id
	  add_index :scaners,:team_id
	  add_index :scaners,:repo_id
  end
end
