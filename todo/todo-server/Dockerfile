FROM ubuntu:14.04

RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

EXPOSE 80

#ENV JAVA_TOOL_OPTIONS -Xmx=256m

ENTRYPOINT ["java", "-jar", "todo-server.jar"]
CMD ["server", "config.yml"]

ADD config/config.yml /
ADD target/todo-server.jar /
