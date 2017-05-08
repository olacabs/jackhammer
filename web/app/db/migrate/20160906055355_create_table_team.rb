class CreateTableTeam < ActiveRecord::Migration
  def change
    create_table :teams do |t|
	    t.string :name
    end
  end
end
