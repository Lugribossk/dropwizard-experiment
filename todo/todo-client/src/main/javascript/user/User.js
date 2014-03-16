define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var TboneModel = require("tbone/TboneModel");
    var Promise = require("tbone/util/Promise");
    var OAuth2AccessToken = require("tbone/auth/OAuth2AccessToken");

    /**
     * @class User
     */
    return TboneModel.extend({
        defaults: {
            name: null,
            email: null
        },

        urlRoot: "http://localhost:8080/users/",

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