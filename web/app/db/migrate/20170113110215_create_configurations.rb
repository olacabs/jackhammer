class CreateConfigurations < ActiveRecord::Migration
  def change
    create_table :configurations do |t|
      t.string :name
      t.string :value

      t.timestamps null: false
    end
    add_index :configurations, :name, unique: true
    Configuration.create(:name => 'admin:paths:plugin_templates', :value => Rails.root.join('templates', 'plugins').to_s)
  end
end
