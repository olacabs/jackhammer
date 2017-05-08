require 'rails_helper'

RSpec.describe FindingsController, type: :controller do
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
		context "list findings" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end
			it "list all scanner findings" do
				scaner = create(:scaner,user_id: normal_user.id)
				get :index,scan_id: scaner.id 
				expect(response).to render_template("index")
			end
		end
	end
	describe "GET #details" do
		context "display finding" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end
			it "display finding details" do
				repo = create(:repo)
				scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
				finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
				get :details,scan_id: scaner.id,finding_id: finding.id
				expect(response).to render_template("details")
			end     
		end     
	end    
	describe "POST #change_status" do
		context "change finding status" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end
			it "close finding" do
				repo = create(:repo)
				scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
				finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
				xhr :post, :change_status,finding_id: finding.id,status: 'Close'
				finding.reload
				expect(finding.status).to eq('Close')
				expect(response).to render_template("change_status")
			end     
		end     
	end    
	describe "POST #change_false_positive" do
		context "mark finding as mark false +ve" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end
			it "Marking to false +ve" do
				repo = create(:repo)
				scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
				finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
				xhr :post, :change_false_positive,finding_id: finding.id
				finding.reload
				expect(finding.is_false_positive).to eq(true)
				expect(response).to render_template("change_false_positive")
			end     
		end     
	end    
	describe "POST #change_to_not_exploitable" do
		context "mark finding as not exploitable" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end
			it "Marking to not exploitable" do
				repo = create(:repo)
				scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
				finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
				xhr :post, :change_to_not_exploitable,finding_id: finding.id
				finding.reload
				expect(finding.not_exploitable).to eq(true)
				expect(response).to render_template("change_to_not_exploitable")
			end     
		end     
	end      
	describe "POST #publish_to_jira" do
		context "move to jira" do
			let(:normal_user) { create(:user)}
			before :each do
				create( :default_role )
				login_with normal_user
			end
			it "Publishing to jira" do
				repo = create(:repo)
				scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
				finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
				xhr :post, :publish_to_jira,finding_id: finding.id
				finding.reload
				if Setting['jira'].present? && Setting['jira']['username'].present? 
					expect(finding.is_publish_to_jira).to eq(true)
				else	
					expect(finding.is_publish_to_jira).to eq(false)
				end
				expect(response).to render_template("publish_to_jira")
			end     
		end     
	end      
	describe "POST #save_tag" do
                context "add tags to finding" do
                        let(:normal_user) { create(:user)}
                        before :each do
                                create( :default_role )
                                login_with normal_user
                        end
                        it "tagging" do
                                repo = create(:repo)
                                scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
                                finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
                                xhr :post, :save_tag,finding_id: finding.id,tag_name: Faker::Name.unique.name 
                                finding.reload
                                expect(finding.tags.count).to eq(1)
                                expect(response).to render_template("save_tag")
                        end     
                end     
        end      
	 describe "PUT  #update" do
               context "update finding" do
                        let(:normal_user) { create(:user)}
                        before :each do
				create( :default_role )
                                login_with normal_user
                        end     
                        it "update severity of finding" do
                                repo = create(:repo)
                                scaner = create(:scaner,user_id: normal_user.id,repo_id: repo.id)
                                finding = create(:finding,scaner_id: scaner.id,repo_id: repo.id,user_id: normal_user.id)
                                xhr :put, :update,{:finding=>{:severity=>"Medium"},id: finding.id}
                                finding.reload
                                expect(finding.severity).to eq('Medium')
				expect(response).to be_success
                        end     
                end     
        end              
end
