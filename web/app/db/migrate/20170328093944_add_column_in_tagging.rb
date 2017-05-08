class AddColumnInTagging < ActiveRecord::Migration
  def change
	  add_column :taggings,:finding_id,:integer
	 # change_column :tags,:name,:text,:length => {:name=>5}
  end
end
