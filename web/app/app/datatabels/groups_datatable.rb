class GroupsDatatable

	delegate :params,:link_to, to: :@view

	def initialize(view)
		@view = view
	end

	def as_json(options = {})
		{
			sEcho: params[:sEcho].to_i,
			iTotalRecords: Team.count,
			iTotalDisplayRecords: teams.total_entries,
			aaData: data
		}
	end

	private

	def data
		teams.map do |team|
			[
				ERB::Util.h(team.name),
                                ERB::Util.h(team.created_at.to_formatted_s(:short)),
				ERB::Util.h(team.updated_at.to_formatted_s(:short)),
				link_to("<i class='fa fa-pencil-square-o text-info' aria-hidden='true'></i>".html_safe,@view.edit_team_path(team),method: :get),
				link_to("<i class='fa fa-times text-danger' aria-hidden='true'></i>".html_safe,@view.team_path(team),method: :delete)
			]
		end
	end

	def teams
		@teams ||= fetch_teams
	end
	def fetch_teams
		teams = Team.order("#{sort_column} #{sort_direction}")
		teams = teams.page(page).per_page(per_page)
		if params[:sSearch].present?
			teams = teams.where("lower(name) like :search", search: "%#{params[:sSearch].downcase}%")
		end
		teams
	end

	def page
		params[:iDisplayStart].to_i/per_page + 1
	end

	def per_page
		params[:iDisplayLength].to_i > 0 ? params[:iDisplayLength].to_i : 10
	end

	def sort_column
		columns = %w[name created_at updated_at]
		columns[params[:iSortCol_0].to_i]
	end

	def sort_direction
		params[:sSortDir_0] == "desc" ? "desc" : "asc"
	end
end
