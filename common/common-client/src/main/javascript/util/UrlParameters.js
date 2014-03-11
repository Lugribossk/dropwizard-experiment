define(function (require) {
    "use strict";
    var _ = require("underscore");

    function join(scope, prefix) {
        var keys = _.keys(scope);

        if (keys.length === 0) {
            return "";
        }

        return prefix + _.map(keys, function (key) {
            return encodeURIComponent(key) + "=" + encodeURIComponent(scope[key]);
        }).join("&");
    }

    /**
     * Query or hash fragment parameter parsing.
     * The parameters are placed directly as properties on the object.
     *
     * @author Bo Gotthardt
     * @constructor
     *
     * @param {String} parameterString The parameters to parse.
     */
    function UrlParameters(parameterString) {
        var scope = this;

        if (parameterString !== "") {
            var parameters = parameterString.split("&");
            _.each(parameters, function (parameter) {
                var keyValue = parameter.split("=");
                if (keyValue.length === 2) {
                    scope[keyValue[0]] = decodeURIComponent(keyValue[1]);
                } else {
                    scope[keyValue[0]] = "";
                }
            });
        }
    }

    /**
     * Create from query parameters.
     *
     * @param {Window} [frame] The window to parse query parameters for. Optional, defaults to the current frame.
     * @returns {UrlParameters}
     */
    UrlParameters.fromQuery = function (frame) {
        frame = frame || window;
        var parameters = frame.location.search;
        return new UrlParameters(parameters.length > 0 ? parameters.substr(1) : "");
    };

    /**
     * Create from hash fragment parameters.
     *
     * @param {Window} [frame] The window to parse hash fragment parameters for. Optional, defaults to the current frame.
     * @returns {UrlParameters}
     */
    UrlParameters.fromHash = function (frame) {
        frame = frame || window;
        // We can't use window.location.hash as a Firefox bug from 2002 (!!!) automatically unescapes it when accessed.
        return new UrlParameters(frame.location.href.split("#")[1] || "");
    };

    /**
     * Returns the parameters as a query string, including the leading ? if applicable.
     *
     * @returns {String}
     */
    UrlParameters.prototype.toQueryString = function () {
        return join(this, "?");
    };

    /**
     * Returns the parameters as a hash fragment, including the leading # if applicable.
     *
     * @returns {String}
     */
    UrlParameters.prototype.toHashFragment = function () {
        return join(this, "#");
    };

    return UrlParameters;
});