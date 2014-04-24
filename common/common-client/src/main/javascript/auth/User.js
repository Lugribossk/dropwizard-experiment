define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var TboneModel = require("common/TboneModel");
    var Promise = require("common/util/Promise");
    var OAuth2AccessToken = require("common/auth/OAuth2AccessToken");

    /**
     * @class User
     */
    return TboneModel.extend({
        defaults: {
            name: null,
            email: null
        },

        urlRoot: "http://localhost:8080/api/users/",

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