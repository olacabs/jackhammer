class AddUserColumnToUpload < ActiveRecord::Migration
  def change
    add_column :uploads, :user_id, :integer
  end
end
