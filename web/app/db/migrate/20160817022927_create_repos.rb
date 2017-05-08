class CreateRepos < ActiveRecord::Migration
  def change
    create_table :repos do |t|
      t.string :name
      t.string :full_name
      t.integer :service_portal,:limit=>1
      t.string :html_url
      t.string :languages
      t.integer :forked,:limit=>1

      t.timestamps null: false
    end
  end
end
