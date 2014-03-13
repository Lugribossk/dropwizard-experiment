/*global module, require*/
module.exports = function (grunt) {
    "use strict";

    // The Grunt task configurations are split into several files for readability.
    grunt.initConfig({});
    require("../../common/common-client/grunt/dev.js")(grunt);
    require("../../common/common-client/grunt/build.js")(grunt);
    require("../../common/common-client/grunt/test.js")(grunt);

    grunt.registerTask("default", ["jshint:dev"]);

    // Maven will run these tasks in the named phase.
    grunt.registerTask("maven-compile", ["build"]);
    grunt.registerTask("maven-test", ["jshint:ci", "karma:ci"]);
};