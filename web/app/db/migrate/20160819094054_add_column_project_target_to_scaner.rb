class AddColumnProjectTargetToScaner < ActiveRecord::Migration
  def change
   add_attachment :scaners, :project_target
  end
end
