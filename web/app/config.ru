# This file is used by Rack-based servers to start the application.
require 'sidekiq/web'
require 'sidekiq-statistic'
require ::File.expand_path('../config/environment', __FILE__)
Sidekiq::Web.use(Rack::Session::Cookie, secret: Rails.application.secrets.secret_key_base)
#Sidekiq::Web.instance_eval { @middleware.rotate!(-1) }
run Sidekiq::Web
run Rails.application

