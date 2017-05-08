class RenameColumnInUser < ActiveRecord::Migration
  def change
	  change_column :users,:is_security? ,:boolean,:default=>false
	  rename_column :users,:is_security? ,:is_security_member?

  end
end
