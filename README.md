Full stack experiment
=====================

##Javascript

###Setup
1. Install NodeJS and npm.
2. Globally install Grunt CLI and Bower: `npm install -g grunt-cli bower`
3. Install individual project dependencies in their folders: `npm install` && `bower install` (Maven will automatically do this.)


##Java

###Unit test setup
Add `-javaagent:${USER_HOME}\.m2\repository\org\avaje\ebeanorm\avaje-ebeanorm-agent\3.2.2\avaje-ebeanorm-agent-3.2.2.jar` to the VM options.

In IntelliJ you can make this the default by adding it under Run - Edit Configurations - Defaults - JUnit.