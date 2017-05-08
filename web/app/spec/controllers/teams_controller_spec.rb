require 'rails_helper'

RSpec.describe TeamsController, type: :controller do
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
	describe "GET #list_groups" do
		context "Not admin" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny team managemnt access for dev/qa users" do
				get :list_groups
				expect(response).to_not render_template("list_groups")
			end
		end
		context "Admin user" do
			before :each do
				login_with create(:admin)
			end
			it "allow team managemnt access for admin" do
				get :list_groups
				expect(response).to render_template("list_groups")                            
			end
		end
	end
	describe "GET #new" do
		context "Not admin" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny adding new team for other than admin" do
				get :new
				expect(response).to_not render_template("new")
			end
		end
		context "Admin user" do
			before :each do
				login_with create(:admin)
			end
			it "allow adding new team for admin" do
				get :new
				expect(response).to render_template("new")
			end
		end
	end
	describe "POST #create" do
		context "Not admin" do
			let(:new_attributes) { FactoryGirl.build(:team, name: Faker::Name.unique.name).attributes.symbolize_keys }
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny creating new team for other than admin" do
				team_count = Team.count
				post :create,{"team"=>new_attributes}
				expect(Team.count).to eq(team_count)
				expect(response).to_not redirect_to( list_groups_teams_path )
			end
		end
		context "Admin user" do
			let(:new_attributes) { FactoryGirl.build(:team, name: Faker::Name.unique.name).attributes.symbolize_keys }
			let(:admin_user) {create(:admin) }
			before :each do
				login_with admin_user
			end
			it "all creating new team for admin" do
				team_count = Team.count
				post :create,{"team"=>new_attributes}
				expect(Team.count).not_to eq(team_count)
				expect(response).to redirect_to( list_groups_teams_path )
			end

		end
	end
	describe "GET #edit" do
		context "Not admin" do
			let(:team) { create(:team)}
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny edting team for other than admin" do
				get :edit,id: team.id
				expect(response).to_not render_template("edit")
			end
		end
		context "Admin user" do
			let(:admin_user) {create(:admin) }
			let(:team) { create(:team)}
			before :each do
				login_with admin_user
			end
			it "allow editing new team for admin" do
				get :edit,id: team.id
				expect(response).to render_template("edit")
			end
		end
	end
	describe "POST #update" do
		context "Not admin" do
			let(:valid_attributes) { FactoryGirl.build(:team).attributes.symbolize_keys }
			let(:new_attributes) { FactoryGirl.build(:team, name: Faker::Name.unique.name).attributes.symbolize_keys }
			before :each do
				create( :default_role )
				login_with create(:user)
			end     
			it "deny updating team for other than admin" do
				team = Team.create! valid_attributes
				post :update,{:team=>new_attributes,:id => team.to_param}
				team.reload
				expect(assigns(:team).attributes.symbolize_keys[:name]).to_not eq(new_attributes[:name])
				expect(response).to_not redirect_to( list_groups_teams_path )
			end
		end             
		context "Admin user" do
			let(:admin_user) {create(:admin) }
			let(:valid_attributes) { FactoryGirl.build(:team).attributes.symbolize_keys }
			let(:new_attributes) { FactoryGirl.build(:team, name: Faker::Name.unique.name).attributes.symbolize_keys }
			before :each do
				login_with admin_user
			end
			it "allow updating team for admin" do
				team = Team.create! valid_attributes
				post :update,{:team=>new_attributes,:id => team.to_param}
				team.reload
				expect(assigns(:team).attributes.symbolize_keys[:name]).to eq(new_attributes[:name])
				expect(response).to redirect_to( list_groups_teams_path )
			end
		end     
	end             
	describe "DELETE #destroy" do
		context "Not admin" do
			let(:normal_user) { create(:user)}
			let(:team) { create(:team)}
			before :each do
				create( :default_role )
				login_with normal_user
			end

			it "deny team deletion for other than admin" do
				team_count = Team.count
				delete :destroy,id: team.id
				expect(response).to_not redirect_to( list_groups_teams_path )
			end
		end
		context "Admin user" do
			let(:admin_user) {create(:admin) }
			let(:team) { create(:team)}
			before :each do
				login_with admin_user
			end
			it "allow team deletion for admin" do
				team_count = Team.count
				delete :destroy,id: team.id
				expect(response).to redirect_to( list_groups_teams_url )
			end
		end
	end

end
