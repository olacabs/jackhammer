class Ability
	include CanCan::Ability

	def initialize(user)
		# Define abilities for the passed in user here. For example:
		#
		user ||= User.new # guest user (not logged in)
		get_roles(user)
		if user.is_admin? || user.is_security_member?
			can :manage, :all
		elsif user.is_team_lead?
			cannot :manage,Permission
			cannot :manage,Functionality
			can :manage,Role
			can :manage,Team
			can :manage,User
			can :manage,Scaner
			can :manage,Tag
		else
			cannot :manage,Permission
			cannot :manage,Functionality
			cannot :manage,Role
			cannot :manage,Team
			cannot :manage,User
			if @functionalities.present? && @functionalities.include?("Scan") && @operations_list.present?
				can :read,Scaner if  @operations_list.include?("scan_read")
				can :create,Scaner if  @operations_list.include?("scan_create")
				can :update,Scaner if  @operations_list.include?("scan_update")
				can :destroy,Scaner if @operations_list.include?("scan_delete")
			else
				cannot :manage,Scaner
			end

		end
		#
		# The first argument to `can` is the action you are giving the user
		# permission to do.
		# If you pass :manage it will apply to every action. Other common actions
		# here are :read, :create, :update and :destroy.
		#
		# The second argument is the resource the user can perform the action on.
		# If you pass :all it will apply to every resource. Otherwise pass a Ruby
		# class of the resource.
		#
		# The third argument is an optional hash of conditions to further filter the
		# objects.
		# For example, here the user can only update published articles.
		#
		#   can :update, Article, :published => true
		#
		# See the wiki for details:
		# https://github.com/CanCanCommunity/cancancan/wiki/Defining-Abilities
	end
	def get_roles(user)
		roles = user.roles
		if roles.present?
			@functionalities = roles.map { |role| role.functionalities }.flatten
			@functionalities = @functionalities.map(&:name)
			@operations_list = roles.map { |role| role.function_operations.split(",") if role.function_operations.present? }.flatten
		end     
	end
end
