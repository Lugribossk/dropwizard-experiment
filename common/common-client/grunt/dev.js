/*global module*/
module.exports = function (grunt) {
    "use strict";

    /**
     * Development utility tasks.
     */

    grunt.loadNpmTasks("grunt-contrib-watch");
    grunt.config.set("watch", {
        options: {
            livereload: true
        },
        js: {
            files: ["src/main/javascript/**/*.js"],
            tasks: ["jshint:dev"]
        },
        templates: {
            files: ["src/main/javascript/**/*.html"]
        }
    });

    grunt.loadNpmTasks("grunt-contrib-connect");
    grunt.config.set("connect", {
        client: {
            options: {
                port: 9090,
                hostname: "*",
                base: "../../",
                keepalive: true,
                livereload: true,
                open: "http://localhost:9090/todo/todo-client/src/main/javascript/"
            }
        }
    });

    grunt.registerTask("serve", ["connect:client"]);
};