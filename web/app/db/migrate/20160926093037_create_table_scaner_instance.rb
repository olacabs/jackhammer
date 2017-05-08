class CreateTableScanerInstance < ActiveRecord::Migration
  def change
    create_table :scaner_instances do |t|
      t.integer :scaner_id
      t.timestamps null: false
    end
    add_column :findings,:scaner_instance_id,:integer
  end
end
