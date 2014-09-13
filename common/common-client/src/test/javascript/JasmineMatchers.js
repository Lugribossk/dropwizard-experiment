define(function (require) {
    "use strict";

    return {
        toContainText: function () {
            return {
                compare: function (actual, expected) {
                    return {
                        pass: actual.find("*:contains(\"" + expected + "\")").length > 0,
                        message: "Expected to find element with text \"" + expected + "\"."
                    };
                }
            };
        },
        toBeInstanceOf: function () {
            return {
                compare: function (actual, expected) {
                    return {
                        pass: actual instanceof expected,
                        message: "Expected to be instance of " + expected + "."
                    };
                }
            };
        }
    };
});