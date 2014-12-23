/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Ajax = require("common/util/Ajax");
    var Promise = require("bluebird");

    describe("Ajax", function () {
        describe("requests", function () {
            var def;
            beforeEach(function () {
                spyOn($, "ajax").and.callFake(function () {
                    def = new $.Deferred();
                    return def.promise();
                });
            });

            it("should use $.ajax().", function () {
                var req = Ajax.get({url: "test"});

                var options = $.ajax.calls.first().args[0];
                expect(options.type).toBe("GET");
                expect(options.url).toBe("test");
            });

            it("should resolve with response.", function (done) {
                var req = Ajax.get({url: "test"});

                def.resolve("test");

                req.then(function (data) {
                    expect(data).toBe("test");
                    done();
                });
            });

            it("should reject with error.", function (done) {
                var req = Ajax.get({url: "test"});

                def.reject({
                    status: 404
                });

                req.catch(function (err) {
                    expect(err.status).toBe(404);
                    done();
                });
            });
        });

        describe("retry logic", function () {
            it("should retry requests that fail with 503.", function (done) {
                var first = true;
                spyOn(Promise, "delay").and.returnValue(Promise.resolve());
                spyOn($, "ajax").and.callFake(function () {
                    var def = new $.Deferred();
                    if (first) {
                        first = false;
                        def.reject({status: 503});
                    } else {
                        def.resolve("test");
                    }
                    return def.promise();
                });
                var req = Ajax.get({url: "test"});

                req.then(function (data) {
                    expect(data).toBe("test");
                    done();
                });
            });
        });
    });
});