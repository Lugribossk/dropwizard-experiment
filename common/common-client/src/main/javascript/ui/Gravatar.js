define(function (require) {
    "use strict";
    var TboneView = require("tbone/TboneView");
    var md5 = require("md5");

    return TboneView.extend({
        template: function () {
            return "";
        },

        tagName: "img",

        className: "gravatar",

        bindings: {
            ":el": {
                attributes: [{
                    observe: "email",
                    name: "src",
                    onGet: function (value) {
                        var hash = md5(value.toLocaleLowerCase());
                        var size = Math.ceil(this.$el.innerHeight() * (window.devicePixelRatio || 1));
                        return "http://www.gravatar.com/avatar/" + hash + "?d=mm&s=" + size;
                    }
                }]
            }
        }
    });
});