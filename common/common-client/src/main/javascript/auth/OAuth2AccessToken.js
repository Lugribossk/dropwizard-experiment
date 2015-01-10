define(function (require) {
    "use strict";
    var Ajax = require("common/util/Ajax");
    var TBoneModel = require("common/TboneModel");

    /**
     * @class OAuth2AccessToken
     */
    return TBoneModel.extend({
        defaults: {
            accessToken: null
        },
        
        urlRoot: "/token",

        addToRequestsFor: function (urlPrefix) {
            var scope = this;
            Ajax.on("request", function (options) {
                if (options.url.indexOf(urlPrefix) === 0) {
                    options.headers.Authorization = "Bearer " + scope.get("accessToken");
                }
            });
        }
    }, {
        fetchByLogin: function (username, password) {
            return this.fetch({
                data: {
                    username: username,
                    password: password,
                    grant_type: "password"
                },
                type: "POST"
            });
        }
    });
});