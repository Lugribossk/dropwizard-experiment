/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var $ = require("jquery");
    var Modal = require("common/ui/bootstrap/Modal");

    describe("Modal", function () {
        var body = $("body");

        afterEach(function () {
            Modal._destroy();
        });

        describe("alert", function () {
            it("should close on Ok click.", function (done) {
                Modal.alert("Test!")
                    .then(function () {
                        expect(body).not.toContainText("Test!");
                        done();
                    });

                expect(body).toContainText("Test!");
                body.find(".btn-primary.ok").click();
            });
        });

        describe("confirm", function () {
            it("should close on Ok click.", function (done) {
                Modal.confirm("Test?")
                    .then(function () {
                        expect(body).not.toContainText("Test?");
                        done();
                    });

                expect(body).toContainText("Test?");
                body.find(".btn-primary.ok").click();
            });

            it("should close and reject on Cancel click.", function (done) {
                Modal.confirm("Test?")
                    .done(null, function () {
                        expect(body).not.toContainText("Test?");
                        done();
                    });

                expect(body).toContainText("Test?");
                body.find(".btn-default.cancel").click();
            });
        });
    });
});