define(function (require) {
    "use strict";
    var Backbone = require("backbone");
    var Logger = require("tbone/util/Logger");
    var Promise = require("tbone/util/Promise");

    var log = new Logger("PreRouteHistory");

    /**
     * Alternate version of Backbone.History that allows a function to be called before every route, that determines
     * whether it should be run or not.
     *
     * This makes it convenient place to add global app authentication logic that e.g. checks whether the user is currently logged in.
     * Using a promise means that it can be used to display a login screen regardless of the route
     * (without re-routing to a special login route), and then resolve the promise after authentication succeeds which
     * will run the route, making it possible to deeplink to any route.
     *
     * @class PreRouteHistory
     * @extends Backbone.History
     */
    return Backbone.History.extend({
        /**
         * Function that will be called before every route. Should return a promise for the route being triggered, and reject if it should be blocked.
         *
         * @abstract
         *
         * @param {String} fragment
         * @returns {Promise}
         */
        preRoute: function (fragment) {
            return Promise.resolved();
        },

        loadUrl: function (fragment) {
            // loadUrl is called to trigger a route from a fragment, so it provides a convenient place to
            // add logic that will be run before every route.
            var scope = this;
            // fragment might be undefined on the very first route, which is solved by defaulting logic in the superclass, which we therefore miss out on.
            var actualFragment = fragment || this.getFragment(fragment);
            var doRouting = this.preRoute(actualFragment);

            if (doRouting.state() === "pending") {
                log.info("Suspended routing of", actualFragment);
            }

            doRouting
                .done(function () {
                    Backbone.History.prototype.loadUrl.call(scope, actualFragment);
                })
                .fail(function () {
                    log.warn("Blocked routing of", actualFragment);
                });
        }
    }, {
        initialize: function () {
            var ThisClass = this;
            Backbone.history = new ThisClass();
        }
    });
});