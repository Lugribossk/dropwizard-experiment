/*global module*/
module.exports = function (grunt) {
    "use strict";

    /**
     * Tasks for producing the final build output.
     */

    grunt.loadNpmTasks("grunt-requirejs");
    grunt.config.set("requirejs", {
        options: {
            baseUrl: "src/javascript",
            mainConfigFile: "src/javascript/require.config.js",
            findNestedDependencies: true,
            almond: true,
            optimize: "uglify2",
            preserveLicenseComments: false,
            generateSourceMaps: true
        },
        build: {
            options: {
                name: "blah",
                out: "target/test.js"
            }
        }
    });

    grunt.loadNpmTasks("grunt-string-replace");
    grunt.config.set("string-replace", {
        html: {
            options: {
                replacements: [{
                    pattern: "${build}",
                    replacement: "<%= revision %> <%= grunt.template.today(\"yyyy/mm/dd HH:MM:ss Z\") %>"
                }, {
                    pattern: /\s*<!-- \${css-start}[\S\s]*?\${css-end} -->/,
                    replacement: "\n<link rel=\"stylesheet\" href=\"styling.css\">"
                }, {
                    pattern: /\s*<!-- \${scripts-start}[\S\s]*?\${scripts-end} -->/,
                    replacement: "\n<script src=\"main.js\"></script>"
                }]
            },
            files: [{
                src: "src/index.html",
                dest: "target/index.html"
            }]
        }
    });

    grunt.loadNpmTasks("grunt-git-describe");
    grunt.config.set("git-describe", {
        options: {
            // Requires version 2.1.0 of the plugin.
            prop: "revision"
        },
        revision: {}
    });

    grunt.loadNpmTasks("grunt-contrib-clean");
    grunt.config.set("clean", {
        build: ["target/*"]
    });


    grunt.registerTask("build", [
        "clean:build",
        "requirejs:build",
        "git-describe:revision",
        "string-replace:html"
    ]);
};