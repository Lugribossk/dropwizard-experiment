files = [
    JASMINE,
    JASMINE_ADAPTER,
    REQUIRE,
    REQUIRE_ADAPTER,

    // Seems like this file must be first or the test run will silently do nothing.
    "src/test/javascript/test-main.js",

    // Serve all code and tests, but do not include and execute it in the served html page.
    {pattern: "src/main/javascript/**/*.js", included: false},
    {pattern: "src/test/javascript/**/*.js", included: false}
];

preprocessors = {
    // Exclude files in the lib folder from code coverage calculations.
    "**/src/main/javascript/!(lib)**/*.js": "coverage"
};

reporters = ["progress", "coverage"];

logLevel = LOG_DEBUG;

// Don't forget to set the CHROME_BIN environment variable.
//browsers = ["Chrome"];

captureTimeout = 20000;

reportSlowerThan = 500;

coverageReporter = {
    type: "html",
    dir: "target/coverage/"
};