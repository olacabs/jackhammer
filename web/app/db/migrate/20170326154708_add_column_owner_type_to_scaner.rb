class AddColumnOwnerTypeToScaner < ActiveRecord::Migration
  def change
    add_column :scaners, :owner_type, :string
  end
end
