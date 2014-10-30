/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var Marionette = require("marionette");
    var PromiseRegion = require("common/view/PromiseRegion");
    var TestUtils = require("test/TestUtils");
    var Promises = require("common/util/Promises");

    describe("PromiseRegion", function () {
        var dom = TestUtils.createTestDom();

        var TestView = Marionette.ItemView.extend({
            template: function () {
                return "test";
            },

            onRender: function () {
                this.options.done();
            }
        });

        it("should show view when promise resolves.", function (done) {
            var region = new PromiseRegion({el: dom.el});
            var deferred = Promises.deferred();

            region.show(deferred.promise);

            deferred.resolve(new TestView({done: done}));
        });

        it("should show normal view.", function (done) {
            var region = new PromiseRegion({el: dom.el});

            region.show(new TestView({done: done}));
        });
    });
});