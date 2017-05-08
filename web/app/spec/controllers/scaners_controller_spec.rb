require 'rails_helper'

RSpec.describe ScanersController, type: :controller do
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
		context "list targets" do
			before :each do
				login_with create(:admin)
			end
			it "allow accessing targets list" do
				get :index
				expect(response).to render_template("index")
			end
		end
	end
	describe "GET #add_target" do
		context "Adding Target" do
			before :each do 
				login_with create(:admin)
			end
			it "display adding target" do
				get :add_target
				expect(response).to render_template("add_target")
			end
		end
	end
	describe "GET #new" do
		context "Adding  target" do
			before :each do
				login_with create(:admin)
			end
			it "Adding new target" do
				get :new
				expect(response).to render_template("new")
			end
		end
	end
	describe "GET #add_dynamic_scans" do
		context "QA/DEV add dynamic target" do
                        before :each do
				create(:default_role)	
                                login_with create(:user)
                        end     
                        it "deny adding dynamic target" do
                                get :add_dynamic_scans
                                expect(response).to_not render_template("add_dynamic_scans")
                        end
                end       
		context "Admin add dynamic target" do
			before :each do
				login_with create(:admin)
			end
			it "allow Adding dynamic scans" do
				get :add_dynamic_scans
				expect(response).to render_template("add_dynamic_scans")
			end
		end
	end
	describe "POST #create" do
		context "Adding target" do
			before :each do
				login_with create(:admin)
			end
			it "Adding source code target" do
				scaner_count = Scaner.count
				post :create,"scaner"=>{"scan_type"=>"Static", "owner_type"=>"personal", "project_title"=>"webgoat", "source"=>"gitlab", "target"=>"https://github.com/OWASP/railsgoat", "branch_name"=>"master", "periodic_schedule"=>""}
				expect(Scaner.count).not_to eq(scaner_count)
				expect(response).to redirect_to(scaners_path(scan_type: "Static",owner_type: "personal"))
			end
		end
	end
	describe "DELETE #destroy" do
                context "destroying other user scaners" do
                        let(:normal_user) { create(:user)}
			let(:normal_user1) { create(:user)}
                        before :each do
                                create( :default_role )
                                login_with normal_user1
                        end

                        it "deny other scaners deletion" do
				scaner =  create(:scaner,user_id: normal_user.id)
				scaner_instance = create(:scaner_instance,scaner_id: scaner.id)
				scaner_count = Scaner.count
                                delete :destroy,id: scaner.id,:format => 'js'
				expect(Scaner.count).to eq(scaner_count)
				expect(response).to be_success
                        end
			it "allow deletion of own scaner" do
                                scaner =  create(:scaner,user_id: normal_user1.id)
				scaner_instance = create(:scaner_instance,scaner_id: scaner.id)
				scaner_instance.save
                                scaner_count = Scaner.count
                                delete :destroy,id: scaner.id,:format => 'js'
                                expect(Scaner.count).not_to eq(scaner_count)
                                expect(response).to be_success
                        end
                end
        end
end
