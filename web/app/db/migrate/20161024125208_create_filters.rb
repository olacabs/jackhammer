class CreateFilters < ActiveRecord::Migration
  def change
    create_table :filters do |t|
      t.string :filter_name
      t.string :filter_values
      t.integer :user_id

      t.timestamps null: false
    end
  end
end
