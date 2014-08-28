define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var LoginForm = require("todo/auth/LoginForm");
    var User = require("common/auth/User");
    var Logger = require("common/util/Logger");
    var OAuth2AccessToken = require("common/auth/OAuth2AccessToken");
    var Promise = require("bluebird");
    var ExampleApi = require("common/api/ExampleApi");

    var log = new Logger("AuthController");
    var STORAGE_KEY = "accessToken";
    var currentUser = new User();

    function useToken(token) {
        token.addToRequestsFor(ExampleApi.getBaseUrl() + "/");

        return User.fetchCurrent()
            .then(function (user) {
                window.localStorage.setItem(STORAGE_KEY, token.get("accessToken"));
                currentUser.clear();
                currentUser.set(user.attributes);
                return user;
            })
            .catch(function (err) {
                if (err.status === 401) {
                    log.info("Saved token was rejected, deleting it.");
                    window.localStorage.removeItem(STORAGE_KEY);
                }
                return Promise.reject(new Error("Unable to get current user from token."));
            });
    }

    function loadFromLocalStorage() {
        var token = window.localStorage.getItem(STORAGE_KEY);
        if (token) {
            return useToken(new OAuth2AccessToken({accessToken: token}))
                .then(function (user) {
                    log.info("Logged in from saved token as", user.get("username"));
                });
        } else {
            return Promise.reject(new Error("No credentials in localStorage."));
        }
    }

    /**
     * @class AuthController
     */
    return Marionette.Controller.extend({
        initialize: function () {
            var scope = this;
            this._loginSuccess = new Promise(function (resolve) {
                scope._loginSuccessResolve = resolve;
            });
        },

        // A new login attempt will use a new instance, so it is ok that we don't reset this.
        _loginSuccess: null,
        _loginSuccessResolve: null,

        tryCredentials: function (username, password) {
            var scope = this;
            return OAuth2AccessToken.fetchByLogin(username, password)
                .then(useToken)
                .then(function (user) {
                    log.info("Logged in as", user.get("username"));
                    scope._loginSuccessResolve();
                })
                .catch(function () {
                    log.info("Login failed with username", username);
                    // Don't reject the loginSuccess promise here, as the user can try to login multiple times.
                    return Promise.reject(new Error("Login failed."));
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

                    return controller._loginSuccess;
                });
        }
    });
});
