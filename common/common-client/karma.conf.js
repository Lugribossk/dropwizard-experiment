/*global module*/
module.exports = function (config) {
    "use strict";
    config.set({
        frameworks: ["requirejs", "jasmine"],
        files: [
            "../../common/common-client/src/main/javascript/require.config.js",
            "src/test/javascript/test-main.js",
            // Serve all the code, but don't include it as script tags. RequireJS will load them.
            {pattern: "../../common/common-client/bower_components/**/*.js", included: false},
            {pattern: "../../common/common-client/src/main/javascript/**/*", included: false},
            {pattern: "src/main/javascript/**/*", included: false},
            {pattern: "src/test/javascript/**/*", included: false}
        ],
        browsers: ["PhantomJS"],
        singleRun: true,
        // Randomize the port in case several CI jobs are running at the same time.
        port: 10000 + Math.round(Math.random() * 1000),
        // Disable the default html2js preprocessor as it screws up the Handlebars HTML files.
        preprocessors: {
            "src/main/javascript/**/*.js": ["coverage"]
        },
        logLevel: "DEBUG",
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