FactoryGirl.define do
  factory :team do
	name Faker::Name.unique.name     
  end
end
