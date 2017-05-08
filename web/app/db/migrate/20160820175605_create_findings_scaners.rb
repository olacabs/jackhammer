class CreateFindingsScaners < ActiveRecord::Migration
  def change
    create_table :findings_scaners do |t|
      t.references :finding, index: true, foreign_key: true
      t.references :scaner, index: true, foreign_key: true
    end
  end
end
