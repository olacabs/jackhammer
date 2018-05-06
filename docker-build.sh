docker-compose build
docker-compose up -d
docker-compose run web bundle exec rake db:create
docker-compose run web bundle exec rake db:migrate
docker-compose run web bundle exec rake db:seed
docker-compose run web figlet -k -w 500 "JACKHAMMER IS UP" 
