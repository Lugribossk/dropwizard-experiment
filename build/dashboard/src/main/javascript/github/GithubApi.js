define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Ajax = require("common/util/Ajax");
    var Repository = require("dashboard/github/Repository");

    var baseUrl = "https://api.github.com";

    // https://developer.github.com/v3/auth/#basic-authentication

    function GithubApi(owner, oauthToken) {
        this.owner = owner;

        Ajax.on("request", function (options) {
            if (options.url.indexOf(baseUrl) === 0) {
                options.headers.Authorization = "Basic " + oauthToken + ":x-oauth-basic";
            }
        });
    }

    GithubApi.prototype.getRepository = function (name) {
        return Repository.fetchByName(name, this.owner);
    };

    return GithubApi;
});