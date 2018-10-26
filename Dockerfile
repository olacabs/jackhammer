FROM openjdk:8
RUN apt-get -y update
RUN apt-get install -y maven
RUN mkdir -p /home/src/jch_server
ENV WORKSPACE /home/src/jch_server
WORKDIR $WORKSPACE
COPY . /home/src/jch_server/
RUN mvn clean install
EXPOSE 8080
CMD java -jar -Xms500m -Xmx2920m $WORKSPACE/target/jch-server.jar server config.yml
