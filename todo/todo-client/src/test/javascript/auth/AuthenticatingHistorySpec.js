/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var Backbone = require("backbone");
    var AuthenticatingHistory = require("todo/auth/AuthenticatingHistory");
    var AuthController = require("todo/auth/AuthController");

    describe("AuthenticatingHistory", function () {
        it("should resolve when the current user is logged in.", function (done) {
            spyOn(AuthController, "getCurrentUser").and.returnValue(new Backbone.Model({isLoggedIn: true}));
            var auth = new AuthenticatingHistory();

            auth.preRoute("test")
                .then(done);
        });

        it("should resolve when the route does not require authentication.", function (done) {
            spyOn(AuthController, "getCurrentUser").and.returnValue(new Backbone.Model({isLoggedIn: false}));
            var auth = new AuthenticatingHistory();

            auth.preRoute("resetpassword")
                .then(done);
        });

        it("should attempt to login when the current user is logged out and the route requires authentication.", function () {
            spyOn(AuthController, "getCurrentUser").and.returnValue(new Backbone.Model({isLoggedIn: false}));
            spyOn(AuthController, "attemptLogin");
            var auth = new AuthenticatingHistory();
            auth.options = {};

            auth.preRoute("test");

            expect(AuthController.attemptLogin).toHaveBeenCalled();
        });
    });
});