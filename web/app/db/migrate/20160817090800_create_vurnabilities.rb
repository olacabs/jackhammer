class CreateVurnabilities < ActiveRecord::Migration
  def change
    create_table :vurnabilities do |t|
      t.integer :repo_id
      t.integer :branch_id
      t.boolean :current
      t.string :description
      t.string :severity
      t.string :fingerprint
      t.string :first_appeared
      t.string :detail
      t.string :scanner
      t.string :file
      t.string :line
      t.string :code

      t.timestamps null: false
    end
  end
end
