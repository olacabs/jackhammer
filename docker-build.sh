docker-compose build
sleep 30
docker-compose run web rake db:create
docker-compose run web rake db:migrate
docker-compose run  -e APPLICATION_MODE=$1 web rake db:seed
docker-compose up -d
docker-compose run web figlet -k -w 500 "JACKHAMMER IS UP" 
