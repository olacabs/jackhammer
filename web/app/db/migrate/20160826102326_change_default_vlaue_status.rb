class ChangeDefaultVlaueStatus < ActiveRecord::Migration
  def change
      change_column :findings,:status,:string,:default=>"New"
  end
end
