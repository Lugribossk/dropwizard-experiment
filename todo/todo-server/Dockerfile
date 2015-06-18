FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y software-properties-common
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
# Auto-accept license.
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer

EXPOSE 80

#ENV JAVA_TOOL_OPTIONS -Xmx=256m

ENTRYPOINT ["java", "-jar", "todo-server.jar"]
CMD ["server", "config.yml"]

ADD config/config.yml /
ADD target/todo-server.jar /
