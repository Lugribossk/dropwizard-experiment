/*global module, require*/
module.exports = function (grunt) {
    "use strict";

    // The Grunt task configurations are split into several files for readability.
    grunt.initConfig({});
    require("../../common/common-client/grunt/dev.js")(grunt);
    require("../../common/common-client/grunt/build.js")(grunt);
    require("../../common/common-client/grunt/test.js")(grunt);
};