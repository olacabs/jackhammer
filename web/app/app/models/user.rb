class User < ActiveRecord::Base

	# Include default devise modules. Others available are:
	# :confirmable, :lockable, :timeoutable and :omniauthable
	devise :database_authenticatable, :registerable,
		:recoverable, :rememberable, :trackable, :validatable
	devise :omniauthable,:omniauth_providers=>[:github]
	after_create :enable_visibility_levels

	has_many :scaners,dependent: :destroy
	has_many :comments
	has_many :tags
	has_and_belongs_to_many :teams
	has_and_belongs_to_many :roles
	has_and_belongs_to_many :tasks,dependent: :destroy
	has_and_belongs_to_many :repos,dependent: :destroy
	has_many :filters,dependent: :destroy
	has_one :notification,dependent: :destroy
	has_many :alert_notifications, dependent: :destroy
	has_many :findings

	DEFAULT_ROLE = 'Dev'

	def self.from_omniauth(auth)
		where(provider: auth.provider,uid: auth.uid).first_or_initialize.tap do |user|
			user.provider = auth.provider
			user.uid = auth.uid
			user.name = auth.info.nickname
			user.token = auth.credentials.token
			user.save
		end
	end
	def enable_visibility_levels
		#self.update(is_team_lead?: true)
		severities = Task.where.not(group: 'AssignedTask')
		severities.each do |sev|
			self.tasks << sev
		end
		default_setting_role = DEFAULT_ROLE
		default_setting_role = Setting['roles']['default_role'] if Setting['roles'].present? && Setting['roles']['default_role'].present?
		self.update(is_team_lead?: true) if Setting['roles'].present? && Setting['roles']['is_team_lead'].try(:to_i) == 1 
		self.update(is_security_member?: true) if Setting['roles'].present? && Setting['roles']['is_security_member'].try(:to_i) == 1
		self.update(is_admin?: true) if Setting['roles'].present? && Setting['roles']['is_admin'].try(:to_i) == 1
		unless self.is_admin? || self.is_team_lead? || self.is_security_member?
			role = Role.find_by_name(default_setting_role)
			self.roles << role
		end
	end
end
