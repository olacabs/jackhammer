class Team < ActiveRecord::Base
  #rolify
  has_many :repos
  has_many :scaners
  has_many :findings
  has_and_belongs_to_many :users
  #has_and_belongs_to_many :roles
  validates_uniqueness_of :name
  scope :get_non_default_team,->{where(is_default_team: false)}
  scope :not_network,->{where.not(name: "Network")}
  scope :not_wordpress,->{where.not(name: "Wordpress")}
end
