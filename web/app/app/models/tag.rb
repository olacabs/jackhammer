class Tag < ActiveRecord::Base
	belongs_to :user
	has_many :taggings
	has_many :findings, through: :taggings
	validates_uniqueness_of :name
end
