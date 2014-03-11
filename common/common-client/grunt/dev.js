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
            files: ["src/javascript/**/*.js"],
            tasks: ["jshint:dev"]
        },
        templates: {
            files: ["src/**/*.html"]
        }
    });
};