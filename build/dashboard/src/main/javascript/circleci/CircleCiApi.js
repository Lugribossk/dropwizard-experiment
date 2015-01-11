define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Ajax = require("common/util/Ajax");

    var baseUrl = "https://circleci.com/api/v1";

    // https://circleci.com/docs/api

    function CircleCiApi(token) {
        Ajax.on("request", function (username, options) {
            if (options.url.indexOf(baseUrl) === 0) {
                options.url += "?circle-token=" + token;
            }
        });
    }

    CircleCiApi.prototype.getRecentBuilds = function (projectName) {

    };

    return CircleCiApi;
});