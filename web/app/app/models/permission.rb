class Permission < ActiveRecord::Base
	
	has_and_belongs_to_many :roles,dependent: :destroy
	has_and_belongs_to_many :functionalities,dependent: :destroy

	validates_uniqueness_of :name
end
