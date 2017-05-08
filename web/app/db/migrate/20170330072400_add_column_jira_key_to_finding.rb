class AddColumnJiraKeyToFinding < ActiveRecord::Migration
  def change
	  add_column :findings,:jira_key,:string
  end
end
