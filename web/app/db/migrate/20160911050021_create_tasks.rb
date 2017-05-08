class CreateTasks < ActiveRecord::Migration
  def change
    create_table :tasks do |t|
      t.string :name
      t.string :group
      t.boolean :is_active?

      t.timestamps null: false
    end
  end
end
