class RenameTablevurnabilitiesTovulnerabilities < ActiveRecord::Migration
  def change
       rename_table :vurnabilities,:vulnerabilities
  end
end
