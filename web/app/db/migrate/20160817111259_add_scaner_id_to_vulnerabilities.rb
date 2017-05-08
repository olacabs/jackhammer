class AddScanerIdToVulnerabilities < ActiveRecord::Migration
  def change
     add_column :vulnerabilities,:scaner_id,:integer
  end
end
