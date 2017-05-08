class ChangeColumnAdminToIsAdmin < ActiveRecord::Migration
  def change
      rename_column :users,:admin,:is_admin?
  end
end
