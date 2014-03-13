define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneModel = require("tbone/TboneModel");
    var Promise = require("tbone/util/Promise");

    /**
     * @class User
     */
    return TboneModel.extend({
        defaults: {
            name: null,
            email: null
        },

        url: "http://localhost:8080/users/",

        computed: {
            isLoggedIn: {
                deps: ["username"],
                value: function (username) {
                    return !!username;
                }
            }
        }
    }, {
        fetchByLogin: function (username, password) {
            var ThisClass = this;
            return $.ajax({
                url: "http://localhost:8080/token?" + $.param({
                    username: username,
                    password: password,
                    grant_type: "password"
                }),
                type: "POST"
            }).then(function (data) {
                var token = data.accessToken;

                $.ajaxPrefilter(function (options) {
                    options.headers = options.headers || {};
                    options.headers.Authorization = "Bearer " + token;
                });

                return ThisClass.fetchById(null, {
                    url: ThisClass.prototype.url + "current"
                });
            });
        }
    });
});