class ModifyJoiningTable < ActiveRecord::Migration
	def change
		drop_table :findings_scaners
		create_join_table :findings, :scaners do |t|
			 t.index [:finding_id, :scaner_id]
			 t.index [:scaner_id, :finding_id]
		end
	end
end
