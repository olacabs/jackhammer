require 'rails_helper'

RSpec.describe AnalyticsController, type: :controller do
	include Devise::Test::ControllerHelpers
	describe "anonymous user" do
		before :each do
			# This simulates an anonymous user
			login_with nil  
		end
		it "should be redirected to signin" do
			get :index,analytics_type: 'corporate'
			expect(response).to redirect_to( new_user_session_path )
		end

	end     
	describe "GET #index" do
		before :each do
			create( :default_role )
			login_with create(:user)
		end
		context "display analytics" do
			it "display corporate analytics" do
				get :index,analytics_type: 'corporate'
				expect(response).to be_success
			end
			 it "display personal analytics" do
                                get :index,analytics_type: 'corporate'
                                expect(response).to be_success
                        end
		end     
	end
end
