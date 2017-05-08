FactoryGirl.define do
  factory :repo do
	ssh_repo_url "https://github.com/OWASP/railsgoat"	    
	name "webgoat"
	git_type "github"
	repo_type "Static"
  end
end
