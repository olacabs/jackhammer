class CreateJoinTableRepoUser < ActiveRecord::Migration
  def change
    create_join_table :repos, :users do |t|
      # t.index [:repo_id, :user_id]
      # t.index [:user_id, :repo_id]
    end
  end
end
