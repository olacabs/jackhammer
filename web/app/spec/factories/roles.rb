FactoryGirl.define do
  factory :default_role ,class: Role do
	name 'dev'    
  end
  factory :new_role,class: Role do
	  name  Faker::Name.unique.name
  end
end
