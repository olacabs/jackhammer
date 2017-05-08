class CreateJoinTableFidningsTags < ActiveRecord::Migration
  def change
    create_join_table :findings, :tags do |t|
       t.index [:finding_id, :tag_id]
       t.index [:tag_id, :finding_id]
    end
  end
end
