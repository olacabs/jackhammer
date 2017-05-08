class Functionality < ActiveRecord::Base
	has_and_belongs_to_many :permissions,dependent: :destroy 
	has_and_belongs_to_many :roles,dependent: :destroy 
	validates_uniqueness_of :name	
end
