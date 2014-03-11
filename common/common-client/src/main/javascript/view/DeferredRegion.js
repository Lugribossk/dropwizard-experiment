define(function (require) {
    "use strict";
    var Marionette = require("marionette");
    var Promise = require("tbone/util/Promise");

    /**
     * @class DeferredRegion
     */
    return Marionette.Region.extend({
        /**
         * Display a view inside the region.
         * Can also take a promise for a view, which will be shown when it resolves.
         * An optional temporary view (e.g. some sort of loading indicator) can be shown immediately.
         *
         * @param {Marionette.View|Promise} viewOrPromise
         * @param {Marionette.View} [temporaryView]
         */
        show: function (viewOrPromise, temporaryView) {
            var scope = this;

            if (Promise.isPromise(viewOrPromise)) {
                if (viewOrPromise.state() === "pending" && temporaryView) {
                    this.show(temporaryView);
                }

                viewOrPromise.done(function (actualView) {
                    scope.show(actualView);
                });
            } else {
                Marionette.Region.prototype.show.call(this, viewOrPromise);
            }
        }
    });
});