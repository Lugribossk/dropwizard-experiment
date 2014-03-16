define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneView = require("common/TboneView");
    var template = require("hbars!./Dashboard");

    return TboneView.extend({
        template: template
    });
});