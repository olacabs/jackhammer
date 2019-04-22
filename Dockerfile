FROM ubuntu:18.04

RUN rm -rf /var/lib/apt/lists/* && apt-get clean && apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && apt-get install -y --no-install-recommends git \
    && apt-get install -y --no-install-recommends openjdk-8-jdk \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

#RUN apt-get update
#RUN apt-get install -y openjdk-8-jdk

#RUN apt-get -y update
#RUN apt-get install -y maven
RUN apt-get update -qq && apt-get install -qqy \
    apt-transport-https \
    ca-certificates \
    curl \
    lxc \
    iptables
RUN curl -sSL https://get.docker.com/ | sh
RUN mkdir -p /home/src/jch_server
ENV WORKSPACE /home/src/jch_server
WORKDIR $WORKSPACE
COPY . /home/src/jch_server/
RUN mvn clean install
EXPOSE 8080
CMD java -jar -Xms500m -Xmx2920m $WORKSPACE/target/jch-server.jar server config.yml
