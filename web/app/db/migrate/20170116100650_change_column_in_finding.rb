class ChangeColumnInFinding < ActiveRecord::Migration
  def change
   	change_column :findings,:external_link,:binary
  end
end
