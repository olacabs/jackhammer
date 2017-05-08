class AddColumnUserIdToComments < ActiveRecord::Migration
  def change
    add_column :comments,:user_id,:integer
  end
end
