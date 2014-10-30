/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var PreRouteHistory = require("common/auth/PreRouteHistory");
    var Promise = require("bluebird");

    describe("PreRouteHistory", function () {
        it("should execute route when preRoute() resolves", function (done) {
            var preRoute = false;
            var route = false;
            var TestHistory = PreRouteHistory.extend({
                preRoute: function (fragment) {
                    expect(fragment).toBe("test");
                    preRoute = true;
                    return Promise.resolve();
                }
            });
            var history = new TestHistory({});
            history.route(/test/, function () {
                route = true;
                expect(preRoute).toBe(true);
                done();
            });

            history.loadUrl("test");
        });

        it("should not execute route when preRoute() rejects", function () {
            var preRoute = false;
            var route = false;
            var TestHistory = PreRouteHistory.extend({
                preRoute: function (fragment) {
                    expect(fragment).toBe("test");
                    preRoute = true;
                    return Promise.reject();
                }
            });
            var history = new TestHistory({});
            history.route(/test/, function () {
                route = true;
            });

            history.loadUrl("test");

            expect(preRoute).toBe(true);
            expect(route).toBe(false);
        });
    });
});