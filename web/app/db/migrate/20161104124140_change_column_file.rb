class ChangeColumnFile < ActiveRecord::Migration
  def change
	  change_column :findings,:file,:binary
  end
end
