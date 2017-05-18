FROM ubuntu:14.04
MAINTAINER JACKHAMMER "jackhammer@olacabs.com"


ENV HOME /root
ENV PATH $HOME/.rbenv/bin:$HOME/.rbenv/shims:$PATH
ENV SHELL /bin/bash

RUN apt-get -y update

# install ruby dependencies
RUN apt-get -y install git curl zlib1g-dev build-essential libssl-dev libreadline-dev libyaml-dev libsqlite3-dev sqlite3 libxml2-dev libxslt1-dev libcurl4-openssl-dev python-software-properties libffi-dev
RUN export GIT_TRACE_PACKET=1
RUN export GIT_TRACE=1
RUN export GIT_CURL_VERBOSE=1
#figlet
RUN apt-get -y install figlet

# install mysql dev
RUN echo "#!/bin/sh\nexit 0" > /usr/sbin/policy-rc.d
RUN apt-get -y install libmysqlclient-dev

# test install mysql client to connect

RUN apt-get -y install mysql-client
# install npm package
RUN apt-get -y install nodejs nodejs-legacy npm
RUN npm install -g nsp
RUN npm install -g retire

#install java 
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install python-software-properties
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install software-properties-common
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
rm -rf /var/cache/oracle-jdk8-installer
RUN apt-add-repository ppa:andrei-pozolotin/maven3
RUN apt-get -y update
RUN apt-get -y --force-yes install maven3
RUN apt-get -y install phantomjs

# install rbenv
RUN git clone https://github.com/sstephenson/rbenv.git /root/.rbenv
RUN git clone https://github.com/sstephenson/ruby-build.git /root/.rbenv/plugins/ruby-build

RUN echo 'eval "$(rbenv init -)"' >> $HOME/.profile
RUN echo 'eval "$(rbenv init -)"' >> $HOME/.bashrc

RUN rbenv install 2.3.0
RUN rbenv global 2.3.0

#install bundler
RUN gem install bundler
#adding gem file
WORKDIR /tmp
ADD app/Gemfile Gemfile
ADD app/Gemfile.lock Gemfile.lock
ADD bundle.sh bundle.sh

#RUN bundle install
RUN bash bundle.sh
RUN apt-get install python-pip -y
RUN pip install -I GitPython==2.1.1
RUN apt-get install nmap -y

# add app
ADD app/ /home/app
#create tmp dir
RUN mkdir -p tmp
RUN mkdir -p tmp/pids
RUN mkdir -p log/scans
WORKDIR /home/app
# expose HTTP
EXPOSE 3000
