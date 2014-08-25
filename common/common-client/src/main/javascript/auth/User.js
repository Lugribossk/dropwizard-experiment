define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var TboneModel = require("common/TboneModel");
    var OAuth2AccessToken = require("common/auth/OAuth2AccessToken");
    var ExampleApi = require("common/api/ExampleApi");

    /**
     * @class User
     */
    return TboneModel.extend({
        defaults: {
            name: null,
            email: null
        },

        url: function () {
            return ExampleApi.getBaseUrl() + "/users/" + this.id;
        },

        computed: {
            isLoggedIn: {
                deps: ["username"],
                value: function (username) {
                    return !!username;
                }
            }
        }
    });
});