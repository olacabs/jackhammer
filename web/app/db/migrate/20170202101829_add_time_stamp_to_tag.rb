class AddTimeStampToTag < ActiveRecord::Migration
	def change
		change_table :tags do |t|
			t.timestamps
		end
	end
end
