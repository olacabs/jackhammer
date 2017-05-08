class CreateUploads < ActiveRecord::Migration
  def change
    create_table :uploads do |t|
      t.integer :finding_id
      t.attachment :file
      t.timestamps null: false
    end
  end
end
