class CreateScanManagers < ActiveRecord::Migration
  def change
    create_table :scan_managers do |t|
      t.integer :user_id
      t.string :target
      t.string :project_title

      t.timestamps null: false
    end
  end
end
