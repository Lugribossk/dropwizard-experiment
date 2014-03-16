define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var PreRouteHistory = require("tbone/auth/PreRouteHistory");
    var Promise = require("tbone/util/Promise");
    var AuthController = require("auth/AuthController");

    /**
     * Backbone.History that authenticates the user before allowing access to any routes.
     *
     * @class AuthenticatingHistory
     */
    return PreRouteHistory.extend({
        preRoute: function (fragment) {
            if (AuthController.getCurrentUser().get("isLoggedIn")) {
                return Promise.resolved();
            } else {
                return AuthController.attemptLogin(this.options.region);
            }
        }
    });
});