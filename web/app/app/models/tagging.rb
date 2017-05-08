class Tagging < ActiveRecord::Base
  belongs_to :finding
  belongs_to :tag
end
