class TeamsDatatable

	delegate :params,:link_to, to: :@view

	def initialize(view,current_user)
		@view = view
		@current_user = current_user
	end

	def as_json(options = {})
		{
			sEcho: params[:sEcho].to_i,
			iTotalRecords: teams.count,
			iTotalDisplayRecords: teams.total_entries,
			aaData: data
		}
	end

	private

	def data
		teams.map do |team|
			[
				ERB::Util.h(link_to team.name,@view.repos_path(team_name: team.name)),
				ERB::Util.h(team.total_count),
				ERB::Util.h(team.critical_count),
				ERB::Util.h(team.high_count),
				ERB::Util.h(team.medium_count),
				ERB::Util.h(team.low_count),
				ERB::Util.h(team.info_count)
			]
		end
	end

	def teams
		@teams ||= fetch_teams
	end
	def fetch_teams
		teams = Team.page(page).per_page(per_page)
		if params[:sSearch].present?
			teams = teams.where("lower(name) like :search", search: "%#{params[:sSearch].downcase}%")
		end
		teams.order("#{sort_column} #{sort_direction}")
	end

	def page
		params[:iDisplayStart].to_i/per_page + 1
	end

	def per_page
		params[:iDisplayLength].to_i > 0 ? params[:iDisplayLength].to_i : 10
	end

	def sort_column
		columns = %w[name total_count critical_count high_count medium_count low_count info_count]
		columns[params[:iSortCol_0].to_i]
	end

	def sort_direction
		params[:sSortDir_0] == "desc" ? "desc" : "asc"
	end
end
