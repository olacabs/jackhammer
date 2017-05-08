class CreatePermissions < ActiveRecord::Migration
	def change
		create_table :permissions do |t|
			t.string :name
			t.string :status

			t.timestamps null: false
		end
		create_join_table :functionalities, :permissions do |t|
		end
		create_join_table :functionalities,:roles do |t|

		end
	end
end
