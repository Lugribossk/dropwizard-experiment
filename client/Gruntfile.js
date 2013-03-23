/*global module*/
module.exports = function (grunt) {
    "use strict";

    grunt.initConfig({
        jslint: {
            files: ["src/main/javascript/**/*.js",
                    "src/test/javascript/**/*.js",
                    "Gruntfile.js"],
            exclude: ["src/main/javascript/lib/*.js"],
            directives: {
                plusplus: true,
                vars: true,
                nomen: true,
                todo: true,
                predef: ["define"]
            },
            options: {
                checkstyle: "target/jslint.xml"
            }
        },
        requirejs: {
            compile: {
                options: {
                    // Insert Almond AMD loader into output, so that we do not need to include the RequireJS script.
                    almond: true,
                    baseUrl: "src/main/javascript",
                    mainConfigFile: "src/main/javascript/config.js",
                    // Path to main.js, relative to the base URL
                    name: "main",
                    // Place output file here, Maven will handle the rest.
                    out: "target/resources/assets/main.out.js",
                    logLevel: 1,
                    optimize: "uglify2",
                    // Must be turned off for source maps to work.
                    preserveLicenseComments: false,
                    generateSourceMaps: true
                }
            }
        }/*,
        testacular: {
            options: {
                configFile: "testacular.conf.js"
            },
            unit: {
            },
            ci: {
                singleRun: true,
                reporters: ["progress", "coverage", "junit"],
                browsers: ["PhantomJS"],
                coverageReporter: {
                    type: "cobertura",
                    dir: "target/coverage/"
                }
            }
        }*/
    });

    grunt.loadNpmTasks("grunt-jslint");
    grunt.loadNpmTasks("grunt-requirejs");

    grunt.registerTask("default", ["jslint", "requirejs"]);

    // Maven will run this task in the compile phase.
    grunt.registerTask("maven-compile", ["requirejs"]);
    // Maven will run this task in the test phase.
    grunt.registerTask("maven-test", ["jslint"]);
};