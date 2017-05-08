class UsersDatatable
	
	delegate :params,:link_to, to: :@view

	def initialize(view,current_user)
		@view = view
		@current_user = current_user
	end

	def as_json(options = {})
		{
			sEcho: params[:sEcho].to_i,
			iTotalRecords: User.count,
			iTotalDisplayRecords: users.total_entries,
			aaData: data
		}
	end

	private

	def data
		users.map do |user|
			[
				ERB::Util.h(user.name),
				ERB::Util.h(user.email),
				ERB::Util.h(user.teams.map(&:name).join("/").truncate(20)),
				ERB::Util.h(user.is_admin?),
				ERB::Util.h(user.is_team_lead?),
				ERB::Util.h(user.is_security_member?),
				link_to("<i class='fa fa-pencil-square-o' aria-hidden='true'></i>".html_safe,@view.edit_user_path(user),method: :GET),
				link_to("<i class='fa fa-times text-danger' aria-hidden='true'></i>".html_safe,user,method: :DELETE,data: { confirm: 'Are you sure?' }) ,
			]
		end
	end

	def users
		@userss ||= fetch_users
	end
	def fetch_users
		if @current_user.is_admin? || @current_user.is_security_member?
		   users = User.order("#{sort_column} #{sort_direction}")
		else
		  teams = @current_user.teams
		  users = @current_user.teams.first.users
		  count = 0 
		  for team in teams 
			  count = count + 1
			  next if count==1
			  users = users | team.users 
		  end
		end
		users = users.order("#{sort_column} #{sort_direction}").page(page).per_page(per_page)
		if params[:sSearch].present?
			users = users.where("lower(name) like :search", search: "%#{params[:sSearch].downcase}%")
		end
		users
	end

	def page
		params[:iDisplayStart].to_i/per_page + 1
	end

	def per_page
		params[:iDisplayLength].to_i > 0 ? params[:iDisplayLength].to_i : 10
	end

	def sort_column
		columns = %w[name email]
		columns[params[:iSortCol_0].to_i]
	end

	def sort_direction
		params[:sSortDir_0] == "desc" ? "desc" : "asc"
	end
end
