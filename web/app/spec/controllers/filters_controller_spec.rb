require 'rails_helper'

RSpec.describe FiltersController, type: :controller do
	include Devise::Test::ControllerHelpers
	describe "anonymous user" do
		before :each do
			# This simulates an anonymous user
			login_with nil
		end
		it "should be redirected to signin" do
			get :apply_filter
			expect( response ).to redirect_to( new_user_session_path )
		end

	end
	describe "GET #apply_filter" do
		context "filters view" do
			before :each do
				login_with create(:admin)
			end
			it "corporate filters" do
				get :apply_filter,:filter_type=> 'corporate'
				expect(response).to render_template("apply_filter")
			end
			it "personal filters" do
				get :apply_filter,:filter_type=> 'personal'
				expect(response).to render_template("apply_filter")
			end
		end
	end
	describe "POST #results" do
		params = {"filter"=>{"tag_id"=>[""], "status"=>[""], "from_date"=>"", "to_date"=>"", "repo_id"=>[""], "severity"=>[""], "description"=>[""], "aging"=>"", "team_id"=>[""], "scanner"=>[""], "static_app"=>"1", "web_app"=>"1", "wordpress_app"=>"1", "mobile_app"=>"1", "network_app"=>"1"},"format"=>'json'}

		context "corporate filters" do
			before :each do
				create(:default_role)
				login_with create(:user)
				session[:filter_type] = 'corporate'
			end
			it "corporate filter results" do
				post :results,params
				expect(response).to be_success
			end
			it "personal filter results" do
				post :results,params
				expect(response).to be_success
			end
		end
	end

end
