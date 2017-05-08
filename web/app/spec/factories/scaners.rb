FactoryGirl.define do
  factory :scaner do
	scan_type "Static" 
	owner_type "personal"
       	project_title "webgoat"
       	source  "gitlab" 
        target "https://github.com/OWASP/railsgoat"
       	branch_name "master" 
  end
end
