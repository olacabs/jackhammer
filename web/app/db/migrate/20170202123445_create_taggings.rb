class CreateTaggings < ActiveRecord::Migration
  def change
    drop_join_table :findings, :tags
    add_column :tags,:user_id,:integer
    create_table :taggings do |t|
      t.belongs_to :comment, index: true, foreign_key: true
      t.belongs_to :tag, index: true, foreign_key: true

      t.timestamps null: false
    end
  end
end
