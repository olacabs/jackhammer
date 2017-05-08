require 'rails_helper'

RSpec.describe SettingsController, type: :controller do
	include Devise::Test::ControllerHelpers
	describe "anonymous user" do
		before :each do
			# This simulates an anonymous user
			login_with nil
		end
		it "should be redirected to signin" do
			get :scanner
			expect( response ).to redirect_to( new_user_session_path )
		end

	end
	describe "GET #scanner" do
		context "list scanner tools for QA/Dev" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end
			it "deny accessing scan tools for QA/Dev" do
				get :scanner
				expect(response).to_not be_success
			end
		end
		context "list scanner tools for admin" do
			before :each do
				login_with create(:admin)
			end     
			it "list scanner tools for admin" do
				get :scanner
				expect(response).to be_success
			end     
		end       
	end
	describe "GET #jira" do
		context "jira configuration for QA/Dev" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end
			it "deny jira configuration QA/Dev" do
				get :jira
				expect(response).to_not be_success
			end     
		end     
		context "jira configuration for admin" do
			before :each do
				login_with create(:admin)
			end     
			it "allow accessing scan tools for admin" do
				get :jira
				expect(response).to be_success
			end     
		end       
	end     
	describe "GET #gitlab" do
		context "gitlab configuration for QA/Dev" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end
			it "deny gitlab details for QA/Dev" do
				get :gitlab
				expect(response).to_not be_success
			end     
		end     
		context "gitlab configuration for Admin" do
			before :each do
				login_with create(:admin)
			end     
			it "allow gitlab configuration for admin" do
				get :gitlab
				expect(response).to be_success
			end     
		end       
	end     
	describe "GET #github" do
		context "github configuration for QA/Dev" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end
			it "deny accessing github details for QA/Dev" do
				get :github
				expect(response).to_not be_success
			end     
		end     
		context "github configuration for QA/Dev" do
			before :each do
				login_with create(:admin)
			end     
			it "github configuration for Admin" do
				get :github
				expect(response).to be_success
			end     
		end       
	end     
	describe "GET #signup_role" do
		context "view default signup role for QA/Dev" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end
			it "deny viewing default signup role" do
				get :signup_role
				expect(response).to_not be_success
			end     
		end     
		context "view default signup role for admin" do
			before :each do
				login_with create(:admin)
			end     
			it "allow default signup role for admin" do
				get :signup_role
				expect(response).to be_success
			end     
		end       
	end     
	describe "GET #get_sender_mail" do
		context "view SMTP settings QA/Dev" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end
			it "deny viewing SMTP settings QA/Dev" do
				get :get_sender_mail
				expect(response).to_not be_success
			end    
		end    
		context "view SMTP settings for admin" do
			before :each do
				login_with create(:admin)
			end    
			it "allow SMTP settings for admin" do
				get :get_sender_mail
				expect(response).to be_success
			end    
		end      
	end    
	describe "POST #update for admin" do
		user_name = Faker::Name.unique.name
		password = Faker::Name.unique.name
		project = Faker::Name.unique.name
		api_end_point = Faker::Name.unique.name
		api_token = Faker::Name.unique.name
		context "updating jira details for admin" do
			before :each do 
				login_with create(:admin)
			end 
			it "allow  updating jira details for admin" do
				xhr :post, :update_details,"settings"=>{"jira"=>{"host"=>"localhost:3000", "username"=>user_name, "password"=>password, "default_project"=>project}}
				jira_options = {
					:site => "localhost:3000",
					:username => user_name,
					:password => password,
					:context_path => '' ,#Setting.jira['context_path'],
					:auth_type => :basic
				}
				jira = JIRA::Client.new(jira_options)
				begin
					jira_project = jira.Project.all.select { |p| p.name.downcase == project.downcase }
					if jira_project.present?
						expect(response).to render_template("update_details")
					else	
						expect(response).to render_template("update_details")
					end
				rescue Exception=>e
					expect(response).to render_template("update_details")
				end
			end
		end
		context "updating gitlab details for admin" do
			before :each do
				login_with create(:admin)
			end
			it "allow  updating gitlab details for admin" do
				xhr :post, :update_details,"settings"=>{"gitlab"=>{"host"=>"localhost:3000", "username"=>user_name, "password"=>password, "api_end_point"=>api_end_point, "api_access_token"=>api_token, "client_id"=>"", "client_secret"=>""}}
				expect(response).to render_template("update_details")
			end
		end
		context "updating github details for admin" do
			before :each do
				login_with create(:admin)
			end
			it "allow  updating github details for admin" do
				xhr :post, :update_details,"settings"=>{"github"=>{"host"=>"localhost:3000", "username"=>user_name, "password"=>password, "api_end_point"=>api_end_point, "api_access_token"=>api_token, "client_id"=>"", "client_secret"=>""}}
				expect(response).to render_template("update_details")
			end
		end
		context "updating signup details for admin" do
			before :each do
				login_with create(:admin)
			end
			it "allow  updating singup role details for admin" do
				xhr :post ,:update_details,"settings"=>{"roles"=>{"default_role"=>'Dev', "is_team_lead"=>"0", "is_admin"=>"0", "is_security_member"=>"0"}}
				expect(response).to render_template("update_details")
			end
		end
		context "updating smtp detailsfor admin" do
			before :each do
				login_with create(:admin)
			end
			it "allow  updating smtp details for admin" do
				xhr :post, :update_details,"settings"=>{"notification"=>{"host"=>"localhost:3000", "address"=>"smtp.gmail.com", "domain"=>"mail.gmail.com", "email"=>Faker::Internet.email, "password"=>password, "port"=>"587", "encryption_type"=>"None", "smtp_authentication"=>"yes"}}
				expect(response).to render_template("update_details")
			end
		end

	end
	describe "POST #update for QA/DEV" do
		user_name = Faker::Name.unique.name
		password = Faker::Name.unique.name
		project = Faker::Name.unique.name
		api_end_point = Faker::Name.unique.name
		api_token = Faker::Name.unique.name
		context "updating setting details for QA/DEV" do
			before :each do
				create( :default_role )
				login_with create(:user)
			end
			it "deny  updating jira details for QA/DEV" do
				post :update_details,"settings"=>{"jira"=>{"host"=>"localhost:3000", "username"=>user_name, "password"=>password, "default_project"=>project}}
				expect(response).to render_template("unauthorized")
			end
			it "deny  updating gitlab details for QA/DEV" do
				post :update_details,"settings"=>{"gitlab"=>{"host"=>"localhost:3000", "username"=>user_name, "password"=>password, "api_end_point"=>api_end_point, "api_access_token"=>api_token, "client_id"=>"", "client_secret"=>""}}
				expect(response).to render_template("unauthorized")
			end
			it "deny  updating github details for QA/DEV" do
				post :update_details,"settings"=>{"gitlab"=>{"host"=>"localhost:3000", "username"=>user_name, "password"=>password, "api_end_point"=>api_end_point, "api_access_token"=>api_token, "client_id"=>"", "client_secret"=>""}}
				expect(response).to render_template("unauthorized")
			end
			it "deny  updating signup role details for QA/DEV" do
				post :update_details,"settings"=>{"roles"=>{"default_role"=>'Dev', "is_team_lead"=>"0", "is_admin"=>"0", "is_security_member"=>"0"}}
				expect(response).to render_template("unauthorized")
			end
			it "deny  updating smtp details for QA/DEV" do
				post :update_details,"settings"=>{"notification"=>{"host"=>"localhost:3000", "address"=>"smtp.gmail.com", "domain"=>"mail.gmail.com", "email"=>Faker::Internet.email, "password"=>password, "port"=>"587", "encryption_type"=>"None", "smtp_authentication"=>"yes"}}
				expect(response).to render_template("unauthorized")
			end
		end
	end
end
