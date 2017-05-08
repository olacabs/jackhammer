class Role < ActiveRecord::Base
        has_and_belongs_to_many :functionalities,dependent: :destroy
	has_and_belongs_to_many :permissions,dependent: :destroy
	has_and_belongs_to_many :users,dependent: :destroy
	belongs_to :resource,
		:polymorphic => true
	validates_uniqueness_of :name 
	validates :resource_type,
		:inclusion => { :in => Rolify.resource_types },
		:allow_nil => true

	scopify
end
