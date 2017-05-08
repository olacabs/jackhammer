class AddColumnPortStatusToFinding < ActiveRecord::Migration
  def change
    add_column :findings, :port_status, :string
    add_column :findings,:host_status,:string
  end
end
