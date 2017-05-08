class AddTagsTable < ActiveRecord::Migration
  def change
    create_table :tags do |t|
            t.string :name
    end
    create_join_table :findings,:tags do |t|
    end
  end
end
