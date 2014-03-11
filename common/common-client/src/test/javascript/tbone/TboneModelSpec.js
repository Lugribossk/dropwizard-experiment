/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var $ = require("jquery");
    var TboneModel = require("tbone/TboneModel");

    describe("TboneModel", function () {
        describe("computed attributes", function () {
            var ComputedModel = TboneModel.extend({
                computed: {
                    name: {
                        deps: ["firstName", "lastName"],
                        value: function (first, last) {
                            return first + " " + last;
                        }
                    }
                },
                url: "/test"
            });
            var model;
            beforeEach(function () {
                model = new ComputedModel({
                    firstName: "John",
                    lastName: "Smith"
                });
            });

            it("should have a value based on their dependencies and value function", function () {
                expect(model.get("name")).toBe("John Smith");
            });

            it("should change value when dependencies change", function () {
                model.set("firstName", "Bob");

                expect(model.get("name")).toBe("Bob Smith");
            });

            it("should trigger change event when changing value", function () {
                var name;
                model.once("change:name", function (a, b) {
                    name = b;
                });

                model.set("firstName", "Bob");
                expect(name).toBe("Bob Smith");
            });

            it("should not be sent to the server when saving", function () {
                spyOn($, "ajax");

                model.save();

                var ajaxData = $.ajax.mostRecentCall.args[0].data;
                expect(ajaxData).toEqual(JSON.stringify({
                    firstName: "John",
                    lastName: "Smith"
                }));
            });

            it("should be included in toJSON() for templating", function () {
                expect(model.toJSON().name).toBe("John Smith");
            });
        });
    });
});