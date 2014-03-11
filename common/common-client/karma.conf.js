/*global module*/
module.exports = function (config) {
    "use strict";
    config.set({
        frameworks: ["requirejs", "jasmine"],
        files: [
            "src/javascript/require.config.js",
            "test/test-main.js",
            // Serve all the code, but don't include it as script tags. RequireJS will load them.
            {pattern: "src/javascript/**/*", included: false},
            {pattern: "bower_components/**/*.js", included: false},
            {pattern: "test/**/*", included: false}
        ],
        browsers: ["PhantomJS"],
        singleRun: true,
        // Randomize the port in case several CI jobs are running at the same time.
        port: 10000 + Math.round(Math.random() * 1000),
        // Disable the default html2js preprocessor as it screws up the Handlebars HTML files.
        preprocessors: {
            "src/javascript/**/*.js": ["coverage"]
        },
        reporters: ["progress", "junit", "coverage"],
        junitReporter: {
            outputFile: "target/test-results.xml"
        },
        coverageReporter: {
            type: "html",
            dir: "target/coverage"
        }
    });
};