/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var Marionette = require("marionette");
    var Handlebars = require("Handlebars");
    var TestUtils = require("test/TestUtils");
    var ListCollection = require("tbone/collection/ListCollection");

    describe("ListCollection", function () {
        it("should create models for the list input", function () {
            var col = new ListCollection(["a", "b", "c"]);

            expect(col.at(0).get("value")).toBe("a");
            expect(col.at(1).get("value")).toBe("b");
            expect(col.at(2).get("value")).toBe("c");
        });

        describe("usage in views", function () {
            var dom = TestUtils.createTestDom();

            it("should work with Handelbars templates", function () {
                var TestItemView = Marionette.ItemView.extend({
                    template: Handlebars.compile("<span>{{this.value}}</span>"),
                    tagName: "li"
                });
                var TestCollectionView = Marionette.CollectionView.extend({
                    itemView: TestItemView,
                    tagName: "ul"
                });
                var col = new ListCollection(["a", "b", "c"]);

                var view = new TestCollectionView({collection: col});
                dom.show(view);

                expect(dom.el.html()).toBe("<ul><li><span>a</span></li><li><span>b</span></li><li><span>c</span></li></ul>");
            });

            it("should allow template helpers", function () {
                var TestItemView = Marionette.ItemView.extend({
                    template: Handlebars.compile("<span>{{this.value}}{{isA this}}</span>"),
                    tagName: "li",
                    templateHelpers: {
                        isA: function () {
                            return this.value === "a" ? " is A" : "";
                        }
                    }
                });
                var TestCollectionView = Marionette.CollectionView.extend({
                    itemView: TestItemView,
                    tagName: "ul"
                });
                var col = new ListCollection(["a", "b", "c"]);

                var view = new TestCollectionView({collection: col});
                dom.show(view);

                expect(dom.el.html()).toBe("<ul><li><span>a is A</span></li><li><span>b</span></li><li><span>c</span></li></ul>");
            });
        });

    });
});