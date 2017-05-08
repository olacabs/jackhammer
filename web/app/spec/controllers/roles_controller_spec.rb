require 'rails_helper'

RSpec.describe RolesController, type: :controller do
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
		context "Not admin" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny role managemnt access for dev/qa users" do
				get :index
				expect(response).to_not render_template("index")
			end
		end
		context "Admin user" do
			before :each do
				login_with create(:admin)
			end
			it "allow role managemnt access for admin" do
				get :index
				expect(response).to render_template("index")                            
			end
		end
	end
	describe "GET #new" do
		context "Not admin" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny adding new role for other than admin" do
				get :new
				expect(response).to_not render_template("new")
			end
		end
		context "Admin user" do
			before :each do
				login_with create(:admin)
			end
			it "allow adding new role for admin" do
				get :new
				expect(response).to render_template("new")
			end
		end
	end
	describe "POST #create" do
		context "Not admin" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny creating new role for other than admin" do
				roles_count = Role.count
				post :create,{:role=>{"name"=> Faker::Name.unique.name, "functionality_ids"=>[""], "permissions"=>{"scan_read"=>"0", "scan_create"=>"0", "scan_delete"=>"0", "upload_read"=>"0", "upload_delete"=>"0"}}}
				expect(Role.count).to eq(roles_count)
				expect(response).to_not redirect_to( roles_path )
			end

		end
		context "Admin user" do
                        let(:admin_user) {create(:admin) }
                        before :each do
                                login_with admin_user
                        end
                        it "allow creating new role for admin" do
				 roles_count = Role.count
                                post :create,{:role=>{"name"=> Faker::Name.unique.name, "functionality_ids"=>[""], "permissions"=>{"scan_read"=>"0", "scan_create"=>"0", "scan_delete"=>"0", "upload_read"=>"0", "upload_delete"=>"2"}}}
                                expect(Role.count).to_not eq(roles_count)
				expect(response).to redirect_to( roles_path )
				
                        end
                end
	end
	describe "GET #edit" do 
		context "Not admin" do
			let(:role) { create(:new_role)}
			before :each do
				create( :default_role )
				login_with create(:user)
			end

			it "deny edting role for other than admin" do
				get :edit,id: role.id
				expect(response).to_not render_template("edit")
			end
		end     
		context "Admin user" do
			let(:admin_user) {create(:admin) }
			let(:role) { create(:new_role)}
			before :each do
				login_with admin_user
			end
			it "allow adding new role for admin" do
				get :edit,id: role.id
				expect(response).to render_template("edit")
			end     
		end     
	end             
	describe "POST #update" do
		context "Not admin" do
			let(:valid_attributes) { FactoryGirl.build(:new_role).attributes.symbolize_keys }
			let(:new_attributes) { FactoryGirl.build(:new_role, name: Faker::Name.unique.name).attributes.symbolize_keys }
			before :each do
				create( :default_role )
				login_with create(:user)
			end
			it "deny updating role for other than admin" do
				role = Role.create! valid_attributes
				post :update,{:role=>new_attributes,:id => role.to_param}
				role.reload
				expect(assigns(:role).attributes.symbolize_keys[:name]).to_not eq(new_attributes[:name])
				expect(response).to_not redirect_to( role_path(role) )
			end
		end     
		context "Admin user" do
			let(:admin_user) {create(:admin) }
			let(:valid_attributes) { FactoryGirl.build(:new_role).attributes.symbolize_keys }
			let(:new_attributes) { FactoryGirl.build(:new_role, name: Faker::Name.unique.name).attributes.symbolize_keys }
			before :each do
				login_with admin_user
			end
			it "allow updating role for admin" do
				role = Role.create! valid_attributes
				post :update,{:role=>new_attributes,:id => role.to_param}
				role.reload
				expect(assigns(:role).attributes.symbolize_keys[:name]).to eq(new_attributes[:name])
				expect(response).to redirect_to( role_path(role) )
			end
		end    
	end
	describe "DELETE #destroy" do
		context "Not admin" do
			let(:normal_user) { create(:user)}
			let(:role) { create(:new_role)} 
			before :each do
				create( :default_role )
				login_with normal_user
			end     

			it "deny role deletion for other than admin" do
				delete :destroy,id: role.id
				expect(response).to_not redirect_to( roles_path )
			end
		end     
		context "Admin user" do
			let(:admin_user) {create(:admin) }
			let(:role) { create(:new_role)}
			before :each do
				login_with admin_user
			end     
			it "allow role deletion for admin" do
				delete :destroy,id: role.id
				expect(response).to redirect_to( roles_path )
			end     
		end     
	end     
end
