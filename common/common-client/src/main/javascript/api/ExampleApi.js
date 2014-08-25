define(function (require) {
    "use strict";
    var baseUrl = "/api";

    return {
        initialize: function (config) {
            baseUrl = config.apiBaseUrl;
        },

        getBaseUrl: function () {
            return baseUrl;
        }
    };
});