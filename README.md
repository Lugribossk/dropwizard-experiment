Full stack experiment
=====================
[![Build Status](https://travis-ci.org/Lugribossk/dropwizard-experiment.svg?branch=master)](https://travis-ci.org/Lugribossk/dropwizard-experiment)

An experiment with creating a full stack application with Backbone, Dropwizard, Ebean, RabbitMQ and Docker.

## Javascript frontend

### Setup
1. Install NodeJS and npm.
2. Globally install Grunt CLI and Bower: `npm install -g grunt-cli bower`
3. Install individual project dependencies in their folders: `npm install` && `bower install` (Maven will also automatically do this when running `mvn install`.)

### Running
The frontend is automatically bundled into the backend when that is built, so it is only necessary to run this directly when developing. 

Run `grunt serve` and open `http://localhost:9090/todo/todo-client/src/main/javascript/index.html`

Livereload can be activated by running `grunt watch` in another terminal.

### Tests
Run with `grunt karma:ci` or `grunt karma:unit` + `grunt karma:unit:run`, or with `maven test`, or via an IDE.

#### IntelliJ
Create Karma run configurations and point them at the `karma.conf.js` configuration file in each project.

## Java backend

### Setup
1. Install Java 8 JDK.
2. Install Erlang VM.
3. Install RabbitMQ.

### Running server
Run `mvn install` in the root of the project and then `java -jar todo\todo-server\target\todo-server-0.0.1-SNAPSHOT.jar server todo\todo-server\config\config.yml`.

#### IntelliJ
Create an IntelliJ run configuration for TodoListApplication, then add program arguments: `server todo\todo-server\config\config.yml`
and VM options: `-javaagent:$USER_HOME$\.m2\repository\org\avaje\ebeanorm\avaje-ebeanorm-agent\4.1.6\avaje-ebeanorm-agent-4.1.6.jar`

### Running message queue workers
As above, but with the argument `workers` instead of `server`.

### Tests
Run `maven test` or via an IDE.
The browser used for integration tests can be selected via the `WEBDRIVER` environment variable, either Firefox, Chrome or PhantomJS (the default).

#### IntelliJ
When creating run configurations for tests, add the same VM options as above.

You can make this the default by adding it under Run - Edit Configurations - Defaults - JUnit.

## Continuous Integration
The following setup works with [Codeship](https://www.codeship.io).
### Environment preparation
```
wget --no-check-certificate -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u20-b26/jdk-8u20-linux-x64.tar.gz
tar xvf jdk-8u20-linux-x64.tar.gz
export JAVA_HOME=/home/rof/clone/jdk1.8.0_20
npm install -g grunt-cli bower
```
### Build
```
mvn versions:set -DnewVersion=1.0.0-$(git rev-parse --short HEAD) -B
mvn install -DskipTests=true -B
```
### Test
```
mvn test -B
```
### Deploy
(Doesn't work yet.)
```
mvn deploy -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD -Ddocker.email=$DOCKER_EMAIL -DskipTests=true -B
```

## FAQ

### What's with the Java agent when running in IntelliJ?
It ensures that Ebean can process the classes that are persisted, even when not running via Maven (where a plugin handles it as part of the build).
