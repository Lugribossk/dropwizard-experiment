/*global module*/
module.exports = function (config) {
    "use strict";

    config.set({
        basePath: "../../",
        frameworks: ["requirejs", "jasmine"],
        files: [
            "common/common-client/src/main/javascript/require.config.js",
            "build/dashboard/src/test/javascript/test-main.js",
            // Serve all the code, but don't include it as script tags. RequireJS will load them.
            {pattern: "common/common-client/bower_components/**/*.js", included: false},
            {pattern: "common/common-client/src/main/javascript/**/*", included: false},
            {pattern: "build/dashboard/src/main/javascript/**/*", included: false},
            {pattern: "build/dashboard/src/test/javascript/**/*", included: false}
        ],
        browsers: ["PhantomJS"],
        singleRun: true,
        // Randomize the port in case several CI jobs are running at the same time.
        port: 10000 + Math.round(Math.random() * 1000),
        preprocessors: {
            "build/dashboard/src/main/javascript/**/*.js": ["coverage"]
        },
        //logLevel: "DEBUG",
        reporters: ["progress", "junit", "coverage"],
        junitReporter: {
            outputFile: "build/dashboard/target/test-results-common.xml"
        },
        coverageReporter: {
            type: "html",
            dir: "build/dashboard/target/coverage"
        }
    });
};
