class AddRequestAndResponseToFinding < ActiveRecord::Migration
  def change
    add_column :findings, :request, :string
    add_column :findings, :response, :string
    add_column :findings, :confidence, :string
  end
end
