class AddColumnGitTypeToRepo < ActiveRecord::Migration
  def change
	  add_column :repos,:git_type,:string
  end
end
