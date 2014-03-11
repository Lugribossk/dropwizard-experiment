define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var PreRouteHistory = require("tbone/PreRouteHistory");
    var Promise = require("tbone/util/Promise");

    return PreRouteHistory.extend({
        preRoute: function (fragment) {
            if (this.options.currentUser.get("isLoggedIn")) {
                return Promise.resolved();
            } else {
                return this.options.controller.attemptLogin();
            }
        }
    });
});