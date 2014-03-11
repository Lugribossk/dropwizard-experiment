/*global module*/
module.exports = function (grunt) {
    "use strict";

    /**
     * Testing and quality related tasks.
     */

    var jsFiles = [
        "src/javascript/**/*.js",
        "test/javascript/**/*.js",
        "Gruntfile.js",
        "grunt/**/*.js"
    ];

    grunt.loadNpmTasks("grunt-contrib-jshint");
    grunt.config.set("jshint", {
        options: {
            jshintrc: ".jshintrc"
        },
        dev: {
            src: jsFiles
        },
        ci: {
            options: {
                reporter: "checkstyle",
                reporterOutput: "target/jshint.xml"
            },
            src: jsFiles
        }
    });

    grunt.loadNpmTasks("grunt-karma");
    grunt.config.set("karma", {
        options: {
            configFile: "karma.conf.js"
        },
        unit: {
            browsers: ["Chrome"],
            singleRun: false,
            preprocessors: {},
            reporters: ["progress"]
        },
        ci: {
            // Empty on purpose, as we're just reusing the karma.conf settings.
        }
    });


    grunt.registerTask("ci", [
        "jshint:ci",
        "karma:ci"
    ]);
};