class RenameColumnHtmlurlToSshUrlRepo < ActiveRecord::Migration
  def change
      rename_column :repos,:html_url,:ssh_repo_url
  end
end
