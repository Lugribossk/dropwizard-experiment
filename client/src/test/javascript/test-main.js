/*global window, require, beforeEach*/
// RequireJS main file to start Karma test runs.
(function () {
    "use strict";

    require.config({
        // Karma serves files from /base, so the baseUrl is that with the path to the RequireJS root appended.
        baseUrl: "/base/src/main/javascript",

        // TODO somehow reusing the existing paths/shim configuration, but add on test-specific stuff?
        paths: {
            jquery: "lib/jquery-1.9.1",
            angular: "lib/angular",
            "angular-mocks": "lib/angular-mocks",
            customMatchers: "/base/src/test/javascript/customMatchers"
        },
        shim: {
            angular: {
                deps: ["jquery"],
                exports: "angular"
            },
            "angular-mocks": {
                deps: ["angular"]
            }
        }
    });

    // Karma has a list of all the files it serves, process those so we can require all the tests (i.e. files that end with "Test.js").
    var allTests = Object.keys(window.__karma__.files).filter(function (file) {
        return (/Test\.js$/).test(file);
    });

    require(["customMatchers", "angular-mocks"], function (customMatchers) {
        // Add the Jasmine custom matchers globally so we don't have to do it in every test.
        beforeEach(function () {
            this.addMatchers(customMatchers);
        });

        require(allTests, function () {
            // Start the test run, nested inside another require call so we know that the two dependencies above have loaded.
            window.__karma__.start();
        });
    });
}());