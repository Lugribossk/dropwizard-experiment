/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TestUtils = require("test/TestUtils");
    var FilterableCompositeView = require("common/view/FilterableCompositeView");

    describe("FilterableCompositeView", function () {
        var dom = TestUtils.createTestDom();

        describe("filtering", function () {
            var ChildView;
            var MyFilterableView;
            var items;
            beforeEach(function () {
                ChildView = Marionette.ItemView.extend({
                    template: function (data) {
                        return data.name;
                    },
                    tagName: "li"
                });
                MyFilterableView = FilterableCompositeView.extend({
                    template: function () {
                        return "<input type=\"text\"><ul></ul>";
                    },
                    childView: ChildView,
                    childViewContainer: "ul"
                });
                items = new Backbone.Collection([{
                    name: "test1"
                }, {
                    name: "test2"
                }, {
                    name: "test3"
                }]);
            });

            it("should show everything by default", function () {
                dom.show(new MyFilterableView({collection: items}));

                expect(dom).toContainText("test1");
                expect(dom).toContainText("test2");
                expect(dom).toContainText("test3");
            });

            it("should only show items that pass filter()", function () {
                var view = new MyFilterableView({collection: items});
                view.filter = function (model) {
                    return model.get("name").indexOf("2") !== -1;
                };

                dom.show(view);

                expect(dom).not.toContainText("test1");
                expect(dom).toContainText("test2");
                expect(dom).not.toContainText("test3");
            });

            it("should do the filtering when the input changes", function () {
                var view = new MyFilterableView({collection: items});
                view.filter = function (model, query) {
                    return model.get("name") === query;
                };

                dom.show(view);
                dom.input("input", "test3");

                expect(dom).not.toContainText("test1");
                expect(dom).not.toContainText("test2");
                expect(dom).toContainText("test3");
            });

            it("should not modify passed collection", function () {
                var view = new MyFilterableView({collection: items});
                view.filter = function (model) {
                    return model.get("name").indexOf("2") !== -1;
                };

                dom.show(view);

                expect(items.pluck("name")).toEqual(["test1", "test2", "test3"]);
            });
        });
    });
});