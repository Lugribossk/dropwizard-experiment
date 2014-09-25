define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var template = require("hbars!./LogItemView");
    var TboneView = require("common/TboneView");

    return TboneView.extend({
        template: template,

        tagName: "li",

        bindings: {
            ".message": "message"
        }
    });
});