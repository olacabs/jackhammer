FactoryGirl.define do
	sequence :email do |n|
		"person#{n}@example.com"
	end
	factory :user do
		email
		password '12345678'
		password_confirmation '12345678' 
	end
	factory :admin,class: User do
		email
		password '12345678'
		password_confirmation '12345678'
		is_admin? true
	end
	factory :security_team,class: User do
		email
		password '12345678'
		password_confirmation '12345678'
		is_security_member? true 
	end      
	factory :team_lead,class: User do
		email
		password '12345678'
		password_confirmation '12345678'
		is_team_lead? true 
	end      
end
