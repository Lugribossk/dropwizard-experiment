define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Promise = require("bluebird");
    var Logger = require("common/util/Logger");
    var Toast = require("common/ui/bootstrap/Toast");

    var log = new Logger("Ajax");

    var maxRetries = 1,
        retryDelay = 5000;

    function doRequest(options, retries) {
        retries = retries || 0;
        var request = Promise.resolve($.ajax.apply($, options));

        if (retries <= maxRetries) {
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
            return request.catch(function (err) {
                Toast.error("There was a problem communicating with the server, please try again later.");
                throw err;
            });
        }
    }

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
        useWithBackbone: function (Backbone) {
            Backbone.ajax = doRequest;
        }
    };
});