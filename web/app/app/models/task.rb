class Task < ActiveRecord::Base
	has_and_belongs_to_many :users,dependent: :destroy
	validates_uniqueness_of :name
	belongs_to :scan_type
end
