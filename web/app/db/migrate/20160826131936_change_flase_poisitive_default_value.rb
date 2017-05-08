class ChangeFlasePoisitiveDefaultValue < ActiveRecord::Migration
  def change
       change_column :findings,:is_false_positive,:boolean,:default=>false
  end
end
