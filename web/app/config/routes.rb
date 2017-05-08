require 'sidekiq/web'
require 'sidekiq-status/web'
Rails.application.routes.draw do
	mount Sidekiq::Web => '/sidekiq'
	resources :tasks do
		collection do
			get 'tools_list'
			post 'update_tool'
			get 'configure_view'
			post 'update_configurations'
		end
	end
	resources :tags
	resources :permissions
	resources :functionalities
	devise_scope :user do
		root :to => 'users/sessions#new'
	end

	devise_for :users,controllers: {registrations: "users/registrations",omniauth_callbacks: "users/omniauth_callbacks"}
	resources :users do
		collection do
			get :autocomplete_user_name
			post :add
		end
	end
	resources :scaners do
		collection do
			get 'run'
			get 'load_projects'
			get 'load_branches'
			post 'store_bulk_scans'
			get 'list_scan_results'
			get 'severity_results'
			get 'scan_wise_results'
			get 'my_scans'
			get 'start_scan_for_all_repo'
			get 'add_dynamic_scans'
			get 'scans_diff'
			get 'run_truffle'
			get 'download_results'
			post 'change_find_status'
			get 'add_target'
			post 'unschedule'
		end
	end
	resources :findings do
		resources :comments
		resources :uploads do
			collection do 
				get 'download_file'
			end
		end
		resources :tags
		collection do
			get 'details'
			post 'change_status'
			post 'add_new_tag'
			post 'add_tag'
			delete 'delete_file'
			post 'change_false_positive'
			post 'publish_to_jira'
			post 'change_to_not_exploitable'
			get 'next_finding'
			post 'save_tag'
		end
	end
	resources :teams do
		collection do
			get 'fetch_projects'
			get 'list_groups'
			get 'repo_summary'
			get 'export_findings'
			get 'list_corpo_groups'
		end
	end
	resources :repos do
		collection do
			 get 'repo_summary'
			 get 'repo_tool_wise_findings'
			 get 'repo_severity_wise_findings'
			 get 'repo_channel_results'
			 get 'get_team_repos'
			 get 'list_apps'
			 get 'false_positive_finds'
			 delete 'delete_repo_channel_results'
		end
	end
	resources :dashboards do
		collection do
			get 'display'
			get 'static'
			get 'web'
			get 'wordpress'
			get 'mobile'
			get 'network'
		end
	end

	resource :settings do
		collection do
			get 'severity'
			get 'scanner'
			get 'jira'
			get 'gitlab'
			get 'github'
			get 'signup_role'
			get 'get_mail_info'
			get 'get_sender_mail'
			get 'pull_corporate_info'
			post 'update_details'
			post 'update_mail_info'
			post 'update_severities'
		end
	end	
	resources :user_management,:controller => :user_management
	resources :roles
	resources :groups
	resources :functionalities
	resources :analytics do
		collection do
			get :load_charts
			post :save_filters
			get :get_desc_wise
		end
	end
	resources :filters do
		collection do
			get :apply_filter
			post :results
		end
	end
	resources :scan_types
	resources :upload_scans do
		collection do
			post 'save_team'
		end
	end
	resources :alert_notifications do
		collection do
			post 'update_alerts'
		end
	end
	get 'restricted', :to=> 'errors#unauthorized'
	%w( 404 422 500 ).each do |code|
		get code, :to => "errors#show", :code => code
	end
	#match '*unmatched_route', :to => 'errors#not_found',via: :all
	# The priority is based upon order of creation: first created -> highest priority.
	# See how all your routes lay out with "rake routes".

	# You can have the root of your site routed with "root"
	# root 'welcome#index'

	# Example of regular route:
	#   get 'products/:id' => 'catalog#view'

	# Example of named route that can be invoked with purchase_url(id: product.id)
	#   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

	# Example resource route (maps HTTP verbs to controller actions automatically):
	#   resources :products

	# Example resource route with options:
	#   resources :products do
	#     member do
	#       get 'short'
	#       post 'toggle'
	#     end
	#
	#     collection do
	#       get 'sold'
	#     end
	#   end

	# Example resource route with sub-resources:
	#   resources :products do
	#     resources :comments, :sales
	#     resource :seller
	#   end

	# Example resource route with more complex sub-resources:
	#   resources :products do
	#     resources :comments
	#     resources :sales do
	#       get 'recent', on: :collection
	#     end
	#   end

	# Example resource route with concerns:
	#   concern :toggleable do
	#     post 'toggle'
	#   end
	#   resources :posts, concerns: :toggleable
	#   resources :photos, concerns: :toggleable

	# Example resource route within a namespace:
	#   namespace :admin do
	#     # Directs /admin/products/* to Admin::ProductsController
	#     # (app/controllers/admin/products_controller.rb)
	#     resources :products
	#   end
end
