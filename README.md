Full stack experiment
=====================

## Javascript

### Setup
1. Install NodeJS and npm.
2. Globally install Grunt CLI and Bower: `npm install -g grunt-cli bower`
3. Install individual project dependencies in their folders: `npm install` && `bower install` (Maven will automatically do this.)


## Java

### Running
Create a run configuration for TodoListApplication, then add program arguments: `server todo\todo-server\src\main\resources\configuration.yml`
and VM options: `-javaagent:$USER_HOME$\.m2\repository\org\avaje\ebeanorm\avaje-ebeanorm-agent\3.2.2\avaje-ebeanorm-agent-3.2.2.jar`

Or run `mvn package` and then `java -jar todo\todo-server\target\todo-server-0.0.1-SNAPSHOT.jar` (with the same arguments as above).

### Test setup
For test run configurations, add the same VM options as above.

This ensures that Ebean is activated for the tests, even when not running them via Maven (where a plugin handles it).

You can make this the default by adding it under Run - Edit Configurations - Defaults - JUnit.