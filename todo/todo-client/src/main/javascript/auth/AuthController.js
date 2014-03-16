define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var LoginForm = require("auth/LoginForm");
    var User = require("tbone/auth/User");
    var Logger = require("tbone/util/Logger");
    var OAuth2AccessToken = require("tbone/auth/OAuth2AccessToken");
    var Promise = require("tbone/util/Promise");

    var log = new Logger("AuthController");
    var STORAGE_KEY = "accessToken";
    var currentUser = new User();

    function useToken(token) {
        token.addToRequestsFor("http://localhost:8080/");
        window.localStorage.setItem(STORAGE_KEY, token.get("accessToken"));
        return User.fetchById("current")
            .then(function (user) {
                currentUser.clear();
                currentUser.set(user.attributes);
                return user;
            });
    }

    function loadFromLocalStorage() {
        var token = window.localStorage.getItem(STORAGE_KEY);
        if (token) {
            return useToken(new OAuth2AccessToken({accessToken: token}))
                .done(function (user) {
                    log.info("Logged in from saved token as", user.get("username"));
                });
        } else {
            return Promise.rejected();
        }
    }

    /**
     * @class AuthController
     */
    return Marionette.Controller.extend({
        initialize: function () {
            this._loginSuccess = new $.Deferred();
        },

        tryCredentials: function (username, password) {
            var scope = this;
            return OAuth2AccessToken.fetchByLogin(username, password)
                .then(useToken)
                .done(function (user) {
                    log.info("Logged in as", user.get("username"));
                    scope._loginSuccess.resolve();
                })
                .fail(function () {
                    log.info("Login failed with username", username);
                    // Don't reject the loginSuccess promise here, as the user can try to login multiple times.
                });
        }
    }, {
        /**
         * Get the current user.
         * This is always the same object, even if the user changes.
         *
         * @returns {User}
         */
        getCurrentUser: function () {
            return currentUser;
        },

        /**
         * Log out the current user.
         */
        logout: function () {
            currentUser.clear();
            window.localStorage.removeItem(STORAGE_KEY);

            // Force navigation to the empty route, even if there already.
            Backbone.history.loadUrl("");
            log.info("Logged out");
        },

        /**
         * Attempt to log in the user.
         * This may happen automatically, or via a login form.
         *
         * @param {DeferredRegion} region The region to show the login form in.
         * @returns {Promise} A promise for the login being successful.
         */
        attemptLogin: function (region) {
            var ThisClass = this;

            return loadFromLocalStorage()
                .then(null, function () {
                    var controller = new ThisClass();
                    region.show(new LoginForm({
                        controller: controller
                    }));

                    return controller._loginSuccess.promise();
                });
        }
    });
});