FROM ubuntu:14.04

# Steps to build image locally on OSX
# 1) $ brew install docker docker-machine
# 2) $ docker-machine create --driver virtualbox --engine-storage-driver devicemapper default-devicemapper
# 3) $ docker-machine start default-devicemapper
# 4) $ eval "$(docker-machine env default-devicemapper)"
# 5) $ docker build --tag bazaarvoice/android-sdk:{tag} --file /path/to/this/Dockerfile
# If that built successfully then when you run "$ docker images"
# you should see something similar to...
# $ docker images
# REPOSITORY                TAG                 IMAGE ID            CREATED             SIZE
# bazaarvoice/android-sdk   {tag}               796e6830192c        2 hours ago         5.203 GB

# Steps to push up a new container
# 1) $ docker login --username {docker_username} --password {docker_password} --email {docker_email}
# 2) $ docker run --tty --interactive bazaarvoice/android-sdk:{tag} /bin/bash
#      root@{container_id}:/opt/workspace# exit
# 3) $ docker commit -m "What changes I made" -a "Author Name" {container_id} bazaarvoice/android-sdk:{tag}
# 4) $ docker push bazaarvoice/android-sdk:{tag}

# Update apt
RUN apt-get update

# Install java8
RUN apt-get install -y software-properties-common && add-apt-repository -y ppa:webupd8team/java && apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer

# Install Deps
RUN dpkg --add-architecture i386 && apt-get update && apt-get install -y --force-yes expect git wget libc6-i386 lib32stdc++6 lib32gcc1 lib32ncurses5 lib32z1 python curl

# Install Android SDK
RUN cd /opt && wget --output-document=android-sdk.tgz --quiet https://dl.google.com/android/android-sdk_r24.4.1-linux.tgz && tar xzf android-sdk.tgz --no-same-owner && rm -f android-sdk.tgz && chown -R root.root android-sdk-linux

# Setup environment
ENV ANDROID_HOME /opt/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools

# Cleaning
RUN apt-get clean

# GO to workspace
RUN mkdir -p /opt/workspace
WORKDIR /opt/workspace

RUN ls $ANDROID_HOME/tools

# Install sdk elements
RUN curl -O https://gist.githubusercontent.com/caseykulm/98b9e4415266999c110ea2c455a058b2/raw/1a11c9ff7f9dc7cefa4152b37b471a65f5518e9b/android_accept_license.sh
RUN which android
RUN ls $ANDROID_HOME/tools
RUN chmod +x android_accept_license.sh
RUN which android
RUN ls $ANDROID_HOME/tools
RUN ./android_accept_license.sh "android update sdk --no-ui --all --filter platform-tools,tools,build-tools-24.0.0,android-24,extra-android-m2repository,extra-google-m2repository" "android-sdk-license-c81a61d9"

RUN which adb
RUN echo $PATH
RUN ls $ANDROID_HOME/tools

# get maven 3.3.9
RUN wget --no-verbose -O /tmp/apache-maven-3.3.9-bin.tar.gz http://mirrors.ocf.berkeley.edu/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz

# verify checksum
RUN echo "516923b3955b6035ba6b0a5b031fbd8b /tmp/apache-maven-3.3.9-bin.tar.gz" | md5sum -c

# install maven
RUN tar xzf /tmp/apache-maven-3.3.9-bin.tar.gz -C /opt/
RUN ls -l /opt/; ls -l /opt/apache-maven-3.3.9
RUN rm -f /tmp/apache-maven-3.3.9-bin.tar.gz
ENV MAVEN_HOME /opt/apache-maven-3.3.9
RUN echo $MAVEN_HOME
RUN ls -l $MAVEN_HOME
ENV PATH $PATH:$MAVEN_HOME/bin

# Setup android maven sdk deployer
RUN mkdir -p /opt/android-maven-build
WORKDIR /opt/android-maven-build
RUN git clone https://github.com/simpligility/maven-android-sdk-deployer.git
WORKDIR /opt/android-maven-build/maven-android-sdk-deployer
RUN mvn install; exit 0
RUN mvn install -P 6.0; exit 0

# Reset workspace
WORKDIR /opt/workspace
