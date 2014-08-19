define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Promise = require("bluebird");

    /**
     * Utility class for setting up Bluebird Promises.
     *
     * @class Promises
     */
    function Promises() {}

    Promises.initialize = function () {
        Backbone.ajax = function () {
            return Promise.resolve($.ajax.apply($, _.toArray(arguments)));
        };
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