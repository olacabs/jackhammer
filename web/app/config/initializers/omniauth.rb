Rails.application.config.middleware.use OmniAuth::Builder do
	  provider :github, ENV['GIT_HUB_CLIENT_ID'], ENV['GIT_HUB_CLIENT_SECRET'], scope: "user,repo,gist"
end
