Full stack experiment
=====================
[![Circle CI](https://circleci.com/gh/Lugribossk/dropwizard-experiment.svg?style=svg)](https://circleci.com/gh/Lugribossk/dropwizard-experiment)

An experiment with creating a full stack application with React, Dropwizard, Ebean, RabbitMQ and Docker.

## Javascript frontend
See [common/common-client](./common/common-client) and [todo/todo-client](./todo/todo-client).

### Setup
1. Install Node.js and npm.
2. Globally install Grunt CLI: `npm install -g grunt-cli`
3. Install dependencies in project root: `npm install` (Maven will also automatically do this when running `mvn install`.)

### Running
The frontend is automatically bundled into the backend when that is built, so it is only necessary to run this directly when developing. 

Run `grunt dev` and open `http://localhost:8080`

### Tests
Run with `grunt test`, `maven test`, or via an IDE.

#### IntelliJ
Create Mocha run configurations and point them at the test folder in each project. See the Grunt setup for the options needed.

## Java backend
See [common/common-server](./common/common-server) and [todo/todo-server](./todo/todo-server).

### Setup
1. Install Java 8 JDK.
2. Install Maven 3.
3. Install RabbitMQ (also needs the Erlang VM).
4. Install Docker (or Boot2docker).

### Running server
Run `mvn install` in the root of the project and then `java -jar todo\todo-server\target\todo-server-0.0.1-SNAPSHOT.jar server todo\todo-server\config\config.yml`.

#### IntelliJ
Create an IntelliJ run configuration for TodoListApplication, then add program arguments: `server todo\todo-server\config\config.yml`
and VM options: `-javaagent:$USER_HOME$\.m2\repository\org\avaje\ebeanorm\avaje-ebeanorm-agent\4.5.3\avaje-ebeanorm-agent-4.5.3.jar`

### Running message queue workers
As above, but with the argument `workers` instead of `server`.

### Tests
Run `maven test` or via an IDE.
The browser used for integration tests can be selected via the `WEBDRIVER` environment variable, either Firefox or Chrome (the default).
PhantomJS is unfortunately not supported as it uses an old version of Selenium which causes classpath issues.

#### IntelliJ
When creating run configurations for tests, add the same VM options as above.

You can make this the default by adding it under Run - Edit Configurations - Defaults - JUnit.

## Deployment

[![Deploy to Tutum](https://s.tutum.co/deploy-to-tutum.svg)](https://dashboard.tutum.co/stack/deploy/)

## FAQ

### What's with the Java agent when running in IntelliJ?
It ensures that Ebean can process the classes that are persisted, even when not running via Maven (where a plugin handles it as part of the build).
