if [ $# -eq 0 ]; then
	echo "Building project with out marathon configuraiton..."
	docker-compose -f docker-compose-with-no-marathon.yml build
	docker-compose -f docker-compose-with-no-marathon.yml up -d
else
	echo "Building project with marathon configuraiton..."
	docker-compose -f docker-compose-with-marathon.yml build
	docker-compose -f docker-compose-with-marathon.yml up -d
fi
