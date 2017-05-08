class AddColumnToFindingsIsFlasePositive < ActiveRecord::Migration
  def change
    add_column :findings,:is_false_positive,:boolean
  end
end
