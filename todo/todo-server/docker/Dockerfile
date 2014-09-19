FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y software-properties-common
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
# Auto-accept license.
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer

EXPOSE 80

#RUN mkdir -p /opt/config/
#VOLUME /opt/config
#ENV JAVA_TOOL_OPTIONS -Xmx=256m

CMD ["--help"]
ENTRYPOINT ["java", "-jar", "${project.build.finalName}.jar"]

ADD ${project.build.finalName}.jar /
