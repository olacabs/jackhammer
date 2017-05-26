class AddArachniParametersToScanner < ActiveRecord::Migration
	def change
		add_column :scaners,:username_param,:text
		add_column :scaners,:password_param,:text
		add_column :scaners,:username_param_val,:text
		add_column :scaners,:password_param_val,:text
		add_column :scaners,:web_login_url,:text
	end
end
