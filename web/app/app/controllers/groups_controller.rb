class GroupsController < ApplicationController
	before_action :set_group, only: [:edit, :update, :destroy]
	def index
		respond_to do |format|
			format.html
			format.json { render json: GroupsDatatable.new(view_context) }
		end

	end
	def new
		@group = Team.new
	end
	def create
		@group = Team.new(name: params[:team][:name])

		respond_to do |format|
			if @group.save
				params[:team][:role_ids].select { |id| id.present? }.each do |role|
					role_obj = Role.find(role)
					@group.roles << role_obj
				end
				format.html { redirect_to groups_path,method: 'GET' ,notice: 'Group was successfully created.' }
			else
				format.html { render :new }
				format.json { render json: @group.errors, status: :unprocessable_entity }
			end
		end
	end

	def edit

	end

	def update
	end

	def destroy

	end

	private
	def set_group
		@group = Team.find(params[:id])
	end
end
