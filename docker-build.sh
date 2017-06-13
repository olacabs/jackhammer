docker-compose build
docker-compose up -d
docker-compose exec mysqldb sh -c 'while ! mysqladmin ping --silent; do sleep 1; done'
docker-compose exec web rake db:migrate
docker-compose run --rm -e APPLICATION_MODE=$1 web rake db:seed
docker-compose exec web figlet -k -w 500 "JACKHAMMER IS UP"
