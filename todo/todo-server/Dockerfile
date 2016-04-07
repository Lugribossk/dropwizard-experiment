FROM jeanblanchard/java:8

EXPOSE 80

#ENV JAVA_TOOL_OPTIONS -Xmx=256m

ENTRYPOINT ["java", "-jar", "todo-server.jar"]
CMD ["server", "config.yml"]

ADD config/*.yml target/todo-server.jar /
