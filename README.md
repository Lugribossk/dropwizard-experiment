Full stack experiment
=====================

An experiment with creating a full stack application with Backbone, Dropwizard and Ebean.

## Javascript frontend

### Setup
1. Install NodeJS and npm.
2. Globally install Grunt CLI and Bower: `npm install -g grunt-cli bower`
3. Install individual project dependencies in their folders: `npm install` && `bower install` (Maven will automatically do this.)

### Running
Run `grunt serve` and open `http://localhost:9090/todo/todo-client/src/main/javascript/index.html`

Livereload for development can be activated by running `grunt watch` in another terminal.

### Tests
Run with `grunt karma:ci` or `grunt karma:unit` + `grunt karma:unit:run`, or with `maven test`, or via an IDE.

#### Test setup in IntelliJ
Create Karma run configurations and point them at the `karma.conf.js` configuration file in each project.

## Java backend

### Setup
1. Install Java 8 JDK.
2. Install Erlang VM.
3. Install RabbitMQ.

### Running
Create an IntelliJ run configuration for TodoListApplication, then add program arguments: `server todo\todo-server\config\config.yml`
and VM options: `-javaagent:$USER_HOME$\.m2\repository\org\avaje\ebeanorm\avaje-ebeanorm-agent\4.1.3\avaje-ebeanorm-agent-4.1.4.jar`

Or run `mvn package` and then `java -jar todo\todo-server\target\todo-server-0.0.1-SNAPSHOT.jar` with the same arguments as above (but not the VM options).

### Tests
Run with `maven test` or via an IDE.

#### Test setup in IntelliJ
When creating run configurations for tests, add the same VM options as above.

This ensures that Ebean is activated for the tests, even when not running them via Maven (where a plugin handles it).

You can make this the default by adding it under Run - Edit Configurations - Defaults - JUnit.
