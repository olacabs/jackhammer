class Comment < ActiveRecord::Base
	belongs_to :finding
	belongs_to :user
end
