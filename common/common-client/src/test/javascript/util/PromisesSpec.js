/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Promise = require("bluebird");

    describe("Promises", function () {
        describe("setup", function () {
            it("should use Bluebird promises for Backbone ajax requests.", function () {
                spyOn($, "ajax").and.returnValue(new $.Deferred());

                var request = Backbone.ajax({});

                expect(request instanceof Promise).toBe(true);
            });

            it("should use Bluebird promises for Marionette deferreds.", function () {
                var def = Marionette.Deferred();

                expect(def.promise instanceof Promise).toBe(true);
            });
        });
    });
});