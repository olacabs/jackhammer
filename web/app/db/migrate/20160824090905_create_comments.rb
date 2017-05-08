class CreateComments < ActiveRecord::Migration
  def change
    create_table :comments do |t|
      t.integer :finding_id
      t.string :message
      t.integer :tag_id

      t.timestamps null: false
    end
  end
end
