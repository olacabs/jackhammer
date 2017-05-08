class Repo < ActiveRecord::Base

  belongs_to :team
  has_many :scaners,dependent: :destroy
  has_many :branches,dependent: :destroy
  has_many :findings,dependent: :destroy
  has_and_belongs_to_many :users

  scope :not_from_git,->{where("git_type is null")}
  scope :personal_repos,->{where("team_id is null")}
  scope :corpo_repos,->{where("team_id is not null")}
  scope :by_repo_type,->(repo_type) {where("repo_type in (?)",repo_type)}
  validates_uniqueness_of :ssh_repo_url 
end
