class CreateScanTypes < ActiveRecord::Migration
  def change
    remove_column :tasks,:scan_type
    create_table :scan_types do |t|
      t.string :name

      t.timestamps null: false
    end
    add_column :tasks,:scan_type_id,:integer
  end
end
