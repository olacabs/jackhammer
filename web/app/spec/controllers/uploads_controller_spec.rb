require 'rails_helper'

RSpec.describe UploadsController, type: :controller do
	include Devise::Test::ControllerHelpers
	describe "anonymous user" do
		let(:normal_user) { create(:user)}
		before :each do
			 create( :default_role )
			# This simulates an anonymous user
			login_with nil  
		end
		it "should be redirected to signin" do
			repo = create(:repo)
			scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
			finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
			post :create,finding_id: finding.id
			expect( response ).to redirect_to( new_user_session_path )
		end

	end
	 describe "POST  #create" do
               context "upload files" do
                        let(:normal_user) { create(:user)}
                        before :each do
                                create( :default_role )
                                login_with normal_user
                        end
                        it "upload POC to finding" do
                                repo = create(:repo)
                                scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
                                finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
                                xhr :post, :create,{:upload=>{:file=>fixture_file_upload('files/arch1.png', 'image/png')},finding_id: finding.id}
                                finding.reload
                                expect(finding.uploads.count).to eq(1)
                                expect(response).to be_success
                        end
                end
        end
end
