define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var PreRouteHistory = require("tbone/auth/PreRouteHistory");
    var Promise = require("tbone/util/Promise");
    var LoginController = require("user/LoginController");

    return PreRouteHistory.extend({
        preRoute: function (fragment) {
            if (LoginController.getCurrentUser().get("isLoggedIn")) {
                return Promise.resolved();
            } else {
                return LoginController.showLoginForm(this.options.region);
            }
        }
    });
});