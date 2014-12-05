define(function (require) {
    "use strict";
    var Promise = require("bluebird");

    /**
     * Utility class for setting up Bluebird Promises.
     *
     * @class Promises
     */
    function Promises() {}

    Promises.useWithMarionette = function (Marionette) {
        Marionette.Deferred = Promises.deferred;
    };

    Promises.deferred = function () {
        var resolve, reject;
        var promise = new Promise(function (a, b) {
            resolve = a;
            reject = b;
        });
        return {
            resolve: resolve,
            reject: reject,
            promise: promise
        };
    };

    return Promises;
});