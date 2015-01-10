define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Promise = require("bluebird");
    var Logger = require("common/util/Logger");

    var log = new Logger("Ajax");

    var maxRetries = 1,
        retryDelay = 5000;

    function doRequest(options, retries) {
        retries = retries || 0;
        var request = Promise.resolve($.ajax(options));
        if (retries < maxRetries) {
            return request.catch(function (err) {
                // Status 0 seems to happens when the request is blocked due to the response not having the right CORS headers (due to the server being down).
                if (err.status === 0 || err.status === 503) {
                    log.info("No response from server, retrying.");
                    return Promise.delay(null, retryDelay)
                        .then(function () {
                            return doRequest(options, retries + 1);
                        });
                } else {
                    throw err;
                }
            });
        } else {
            return request;
        }
    }

    var callbacks = [];

    $.ajaxPrefilter(function (options) {
        if (!options.headers) {
            options.headers = {};
        }
        _.each(callbacks, function (callback) {
            callback(options);
        });
    });

    /**
     * @class Ajax
     */
    return {
        request: function (options) {
            return doRequest(options);
        },
        get: function (options) {
            return doRequest(_.extend({type: "GET"}, options));
        },
        post: function (options) {
            return doRequest(_.extend({type: "POST"}, options));
        },
        put: function (options) {
            return doRequest(_.extend({type: "PUT"}, options));
        },
        "delete": function (options) {
            return doRequest(_.extend({type: "DELETE"}, options));
        },

        on: function (name, callback) {
            // It would be better to ue Backbone.Events, but that would introduce a circular dependency.
            if (name === "request") {
                callbacks.push(callback);
            }
        },

        useWithBackbone: function (Backbone) {
            var scope = this;
            // Explicitly use this.request so it can be spied on.
            Backbone.ajax = function () {
                return scope.request.apply(null, arguments);
            };
        }
    };
});