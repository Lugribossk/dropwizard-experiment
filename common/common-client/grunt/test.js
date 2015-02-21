/*global module, process*/
module.exports = function (grunt) {
    "use strict";

    /**
     * Testing and quality related tasks.
     */

    //var jsFiles = [
    //    "src/main/javascript/**/*.js",
    //    "src/test/javascript/**/*.js",
    //    "Gruntfile.js",
    //    "grunt/**/*.js"
    //];
    //
    //grunt.loadNpmTasks("grunt-contrib-jshint");
    //grunt.config.set("jshint", {
    //    options: {
    //        jshintrc: "../../.jshintrc"
    //    },
    //    dev: {
    //        src: jsFiles
    //    },
    //    ci: {
    //        options: {
    //            reporter: "checkstyle",
    //            reporterOutput: "target/jshint.xml"
    //        },
    //        src: jsFiles
    //    }
    //});
    //
    //grunt.loadNpmTasks('grunt-eslint');
    //grunt.config.set("eslint", {
    //    options: {
    //        configFile: '.eslintrc'
    //    },
    //    all: {
    //        src: ["src/**/*.js", "test/**/*.js"]
    //    }
    //});

    grunt.loadNpmTasks("grunt-mocha-test");
    grunt.config.set("mochaTest", {
        options: {
            require: [
                "babel-core/register",
                "./src/test/javascript/testSetup"
            ]
        },
        dev: {
            src: ["src/test/javascript/**/*Test.js"]
        }
    });

    grunt.registerTask("test", ["grunt-mocha-test:dev"]);
    grunt.registerTask("maven-test", ["test"]);
};
