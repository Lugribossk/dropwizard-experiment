define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Logger = require("common/util/Logger");
    var Dashboard = require("todo/dashboard/Dashboard");
	var PasswordResetController = require("todo/auth/reset/PasswordResetController");

    var log = new Logger("ExampleRouter");

    return Backbone.Router.extend({
        routes: {
            "": function () {
                this.region.show(new Dashboard({}));
            },
            "test1": function () {

            },
            "test2/:id": function (id) {

            },
            "resetpassword": function () {
				PasswordResetController.showRequestResetPasswordForm(this.region);
            },
            "verify/:id": function (id) {
				PasswordResetController.showDoPasswordResetForm(this.region, id);
            },
            "*unmatched": function (route) {
                log.warn("Unmatched route", route);
                Backbone.history.navigate("", true);
            }
        },

        initialize: function (options) {
            this.region = options.region;
        }
    });
});