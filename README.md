Full stack experiment
=====================
[![Circle CI](https://circleci.com/gh/Lugribossk/dropwizard-experiment.svg?style=svg)](https://circleci.com/gh/Lugribossk/dropwizard-experiment)

An experiment with creating a full stack application with Backbone/Marionette, Dropwizard, Ebean, RabbitMQ and Docker.

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
and VM options: `-javaagent:$USER_HOME$\.m2\repository\org\avaje\ebeanorm\avaje-ebeanorm-agent\4.5.1\avaje-ebeanorm-agent-4.1.6.jar`

### Running message queue workers
As above, but with the argument `workers` instead of `server`.

### Tests
Run `maven test` or via an IDE.
The browser used for integration tests can be selected via the `WEBDRIVER` environment variable, either Firefox or Chrome (the default).
PhantomJS is unfortunately not supported as it uses an old version of Selenium which causes classpath issues.

#### IntelliJ
When creating run configurations for tests, add the same VM options as above.

You can make this the default by adding it under Run - Edit Configurations - Defaults - JUnit.

## FAQ

### What's with the Java agent when running in IntelliJ?
It ensures that Ebean can process the classes that are persisted, even when not running via Maven (where a plugin handles it as part of the build).
