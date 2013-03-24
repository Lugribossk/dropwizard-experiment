/*global window, require, beforeEach*/
// RequireJS main file to start Karma test runs.
(function () {
    "use strict";

    require.config({
        // Karma serves files from /base, so the baseUrl is that with the path to the RequireJS root appended.
        baseUrl: "/base/src/main/javascript"
    });

    // Karma has a list of all the files it serves, process those so we can require all the tests (i.e. files that end with "Test.js").
    var allTests = Object.keys(window.__karma__.files).filter(function (file) {
        return (/Test\.js$/).test(file);
    });

    require(["/base/src/test/javascript/customMatchers.js"].concat(allTests), function (customMatchers) {
        // Add the Jasmine custom matchers globally so we don't have to do it in every test.
        beforeEach(function () {
            this.addMatchers(customMatchers);
        });

        // Start the test run.
        window.__karma__.start();
    });
}());