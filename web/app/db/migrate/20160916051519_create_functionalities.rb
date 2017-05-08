class CreateFunctionalities < ActiveRecord::Migration
  def change
    create_table :functionalities do |t|
      t.string :name
      t.boolean :is_active?,:default=>1

      t.timestamps null: false
    end
  end
end
