define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var LoginForm = require("user/LoginForm");
    var User = require("user/User");
    var Logger = require("tbone/util/Logger");
    var OAuth2AccessToken = require("tbone/auth/OAuth2AccessToken");

    var log = new Logger("LoginController");
    var currentUser = new User();

    return Marionette.Controller.extend({
        initialize: function () {
            this._loginSuccess = new $.Deferred();
        },

        tryCredentials: function (username, password) {
            var scope = this;
            return OAuth2AccessToken.fetchByLogin(username, password)
                .then(function (token) {
                    token.addToRequestsFor("http://localhost:8080/");
                    return User.fetchById("current");
                })
                .done(function (user) {
                    currentUser.clear();
                    currentUser.set(user.attributes);
                    log.info("Logged in as", user.get("username"));
                    scope._loginSuccess.resolve();
                })
                .fail(function () {
                    log.info("Login failed with username", username);
                    // Don't reject the loginSuccess promise here, ao the user can try to login multiple times.
                });
        }
    }, {
        getCurrentUser: function () {
            return currentUser;
        },
        
        logout: function () {
            currentUser.clear();

            // Force navigation to the empty route, even if there already.
            Backbone.history.loadUrl("");
            log.info("Logged out");
        },

        showLoginForm: function (region) {
            var ThisClass = this;
            var controller = new ThisClass();

            region.show(new LoginForm({
                controller: controller
            }));

            return controller._loginSuccess.promise();
        }
    });
});