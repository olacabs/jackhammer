FactoryGirl.define do
	factory :functionality do
		["Scan","Upload Files","Mark False Positive","Comments","Change Vulnerable Status","Tagging"].each { |f| f}
	end
end
