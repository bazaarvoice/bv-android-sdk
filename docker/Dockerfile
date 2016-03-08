FROM jacekmarchwicki/android

# get maven 3.2.2
RUN wget --no-verbose -O /tmp/apache-maven-3.2.2.tar.gz http://archive.apache.org/dist/maven/maven-3/3.2.2/binaries/apache-maven-3.2.2-bin.tar.gz

# verify checksum
RUN echo "87e5cc81bc4ab9b83986b3e77e6b3095 /tmp/apache-maven-3.2.2.tar.gz" | md5sum -c

# install maven
RUN tar xzf /tmp/apache-maven-3.2.2.tar.gz -C /opt/
RUN ln -s /opt/apache-maven-3.2.2 /opt/maven
RUN ln -s /opt/maven/bin/mvn /usr/local/bin
RUN rm -f /tmp/apache-maven-3.2.2.tar.gz
ENV MAVEN_HOME /opt/maven

# Setup android maven sdk deployer
RUN mkdir -p /opt/android-maven-build
WORKDIR /opt/android-maven-build
RUN git clone https://github.com/simpligility/maven-android-sdk-deployer.git
WORKDIR /opt/android-maven-build/maven-android-sdk-deployer
RUN mvn install; exit 0

# Reset workspace
WORKDIR /opt/workspace
