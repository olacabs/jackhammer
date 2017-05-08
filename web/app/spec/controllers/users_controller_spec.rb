require 'rails_helper'

RSpec.describe UsersController, type: :controller do
	include Devise::Test::ControllerHelpers
	describe "anonymous user" do
		before :each do
			# This simulates an anonymous user
			login_with nil  
		end
		it "should be redirected to signin" do
			get :index
			expect( response ).to redirect_to( new_user_session_path )
		end

	end
	describe "GET #index" do
		context "Normal user" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny user managemnt access for dev/qa users" do
				get :index
				expect(response).to_not render_template("index")
			end
		end
		context "Admin user" do
			before :each do
				login_with create(:admin)
			end
			it "allow user managemnt access for admin" do
				get :index
				expect(response).to render_template("index")				
			end
		end
		context "Security team" do
			before :each do
				login_with create(:security_team)
			end
			it "allow user managemnt access for security team" do
				get :index
				expect(response).to render_template("index")                            
			end
		end
		context "Team lead" do
			before :each do
				login_with create(:team_lead)
			end
			it "allow user managemnt access for team lead" do
				get :index
				expect(response).to render_template("index")                            
			end
		end
	end
	describe "GET #new" do
		context "Normal user" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny user managemnt access for dev/qa users" do
				get :new
				expect(response).to_not render_template("new")
			end
		end
		context "Admin user" do
			before :each do
				login_with create(:admin)
			end
			it "allow user managemnt access for admin" do
				get :new
				expect(response).to render_template("new")                            
			end
		end
		context "Security team" do
			before :each do
				login_with create(:security_team)
			end
			it "allow user managemnt access for security team" do
				get :new
				expect(response).to render_template("new")                            
			end
		end
		context "Team lead" do
			before :each do
				login_with create(:team_lead)
			end
			it "allow user managemnt access for team lead" do
				get :new
				expect(response).to render_template("new")                            
			end
		end
	end
	describe "GET #edit" do
		context "Normal user" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end

			it "deny user edit for dev/qa users" do
				get :edit,id: normal_user.id
				expect(response).to_not render_template("edit")
			end
		end
		context "Admin user" do
			let(:admin_user) {create(:admin) }
			before :each do
				login_with admin_user
			end
			it "allow user edit for admin" do
				get :edit,id: admin_user.id
				expect(response).to render_template("edit")  
			end
		end
		context "Security team" do
			let(:security_team) {create(:security_team)}
			before :each do
				login_with create(:security_team)
			end
			it "allow user edit for security team" do
				get :edit,id: security_team.id
				expect(response).to render_template("edit")  
			end
		end
		context "Team lead" do
			let(:team_lead) {create(:team_lead)}
			before :each do
				login_with team_lead
			end
			it "allow user edit for team lead" do
				get :edit,id: team_lead.id
				expect(response).to render_template("edit")                        
			end
		end
	end

	describe "DELETE #destroy" do
		context "Normal user" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end

			it "deny user deletion for dev/qa users" do
				delete :destroy,id: normal_user.id
				expect(response).to_not redirect_to( users_url )
			end
		end
		context "Admin user" do
			let(:admin_user) {create(:admin) }
			let(:delete_user) { create(:team_lead) }
			before :each do
				login_with admin_user
			end
			it "allow user deletion for admin" do
				delete :destroy,id: delete_user.id
				expect(response).to redirect_to( users_url )
			end
		end
		context "Security team" do
			let(:security_team) {create(:security_team)}
			before :each do
				login_with create(:security_team)
			end
			it "deny user deletion for security team" do
				delete :destroy,id: security_team.id
				expect(response).to_not redirect_to( users_url )
			end
		end
		context "Team lead" do
			let(:team_lead) {create(:team_lead)}
			before :each do
				login_with team_lead
			end
			it "deny user deletion for team lead" do
				delete :destroy,id: team_lead.id
				expect(response).to_not redirect_to( users_url )
			end
		end
	end

end
