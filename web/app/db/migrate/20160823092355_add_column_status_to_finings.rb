class AddColumnStatusToFinings < ActiveRecord::Migration
  def change
     add_column :findings,:status,:string,:default=>"Open"
  end
end
