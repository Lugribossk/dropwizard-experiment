define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var LoginForm = require("example/user/LoginForm");
    var User = require("example/user/User");
    var Logger = require("tbone/util/Logger");

    var log = new Logger("LoginController");

    return Marionette.Controller.extend({
        initialize: function (options) {
            options.currentUser.logout = this.logout.bind(this);
        },

        attemptLogin: function () {
            this.options.region.show(new LoginForm({
                controller: this
            }));

            this._loginSuccess = new $.Deferred();

            return this._loginSuccess.promise();
        },

        tryCredentials: function (username, password) {
            var scope = this;
            return User.fetchByLogin(username, password)
                .done(function (user) {
                    log.info("Logged in as", user.get("email"));
                    scope.options.currentUser.clear();
                    scope.options.currentUser.set(user.attributes);
                    scope._loginSuccess.resolve();
                })
                .fail(function () {
                    log.info("Login failed with username", username);
                    scope._loginSuccess.reject();
                });
        },

        logout: function () {
            log.info("Logged out");
            this.options.currentUser.clear();

            // Force navigation to the empty route, even if there already.
            Backbone.history.loadUrl("");
        }
    });
});