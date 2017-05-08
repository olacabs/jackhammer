FactoryGirl.define do
  factory :finding do
	description "SQL Injection"    
	severity "Critical"
	fingerprint "2ca63386effbf4685000fcecc0211e7700"
	scanner "Brakeman"
	file "Gemfile.lock"
	owner_type "personal"
	scan_type "Static"
  end
end
