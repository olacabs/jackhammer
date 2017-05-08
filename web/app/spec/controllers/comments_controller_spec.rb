require 'rails_helper'

RSpec.describe CommentsController, type: :controller do
	include Devise::Test::ControllerHelpers
	describe "anonymous user" do
		before :each do
			# This simulates an anonymous user
			login_with nil  
		end
		it "should be redirected to signin" do
			get :index,:finding_id=>1
			expect( response ).to redirect_to( new_user_session_path )
		end
	end
	describe "POST  #create" do
		context "Adding comments" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end
			it "create comments for finding" do
				repo = create(:repo)
				scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
				finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
				xhr :post, :create,{:comment=>{:message=>Faker::Name.unique.name},finding_id: finding.id}
				finding.reload
				expect(finding.comments.count).to eq(1)
				expect(response).to be_success
			end
		end
	end
end
