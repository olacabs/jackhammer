class AddColumnGemNameToTask < ActiveRecord::Migration
  def change
	  add_column :tasks,:gem_name,:string
  end
end
