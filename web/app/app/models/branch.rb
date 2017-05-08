class Branch < ActiveRecord::Base
  belongs_to :repo
  has_many :findings
  has_many :scaners
end
