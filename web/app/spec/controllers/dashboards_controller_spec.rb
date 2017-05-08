require 'rails_helper'

RSpec.describe DashboardsController, type: :controller do
	include Devise::Test::ControllerHelpers
	describe "anonymous user" do
		before :each do
			# This simulates an anonymous user
			login_with nil	
		end
		it "should be redirected to signin" do
			get :display,dashboard_type: 'personal',scan_type: 'Static'
			expect( response ).to redirect_to( new_user_session_path )
		end

	end
	describe "GET #display" do
		before :each do
			create( :default_role )
			login_with create(:user)
		end
		context "access all kind of dashboards" do
			onwer_types = ["corporate","team","personal"]
			scan_types = ["Static","Web","Mobile","Wordpress","Network"]
			onwer_types.each do |owner_type|
				scan_types.each do |scan_type|
					it "should let user access #{owner_type} #{scan_type} dashboard " do
						get :display,dashboard_type: owner_type,scan_type: scan_type
						expect( response ).to render_template( :list_dashboards )
					end
				end
			end
		end
	end
end
