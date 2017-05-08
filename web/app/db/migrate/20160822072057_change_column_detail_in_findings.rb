class ChangeColumnDetailInFindings < ActiveRecord::Migration
  def change
   change_column :findings, :detail, :text, :limit => 4294967295
  end
end
