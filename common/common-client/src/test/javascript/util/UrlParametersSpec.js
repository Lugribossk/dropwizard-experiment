/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var UrlParameters = require("common/util/UrlParameters");

    describe("UrlParameters", function () {
        describe("fromQuery()", function () {
            it("should parse parameters", function () {
                var params = UrlParameters.fromQuery({
                    location: {
                        search: "?test1=a&test2=b"
                    }
                });

                expect(params.test1).toBe("a");
                expect(params.test2).toBe("b");
            });

            it("should decode parameter values", function () {
                var params = UrlParameters.fromQuery({
                    location: {
                        search: "?test=" + encodeURIComponent(" &abc=1")
                    }
                });

                expect(params.test).toBe(" &abc=1");
                expect(params.abc).not.toBeDefined();
            });
        });

        describe("fromHash()", function () {
            it("should not use location.hash", function () {
                var params = UrlParameters.fromHash({
                    location: {
                        href: "blah#test1=a&test2=b"
                    }
                });

                expect(params.test1).toBe("a");
                expect(params.test2).toBe("b");
            });
        });

        describe("toQueryString()", function () {
            it("should create query string", function () {
                var params = new UrlParameters("test1=a&test2=b");

                expect(params.toQueryString()).toBe("?test1=a&test2=b");
            });

            it("should encode values", function () {
                var input = "test=" + encodeURIComponent(" &abc=1");
                var params = new UrlParameters(input);

                expect(params.toQueryString()).toBe("?" + input);
            });
        });

        describe("toHashFragment()", function () {
            it("should create hash fragment string", function () {
                var params = new UrlParameters("test1=a&test2=b");

                expect(params.toHashFragment()).toBe("#test1=a&test2=b");
            });
        });
    });
});