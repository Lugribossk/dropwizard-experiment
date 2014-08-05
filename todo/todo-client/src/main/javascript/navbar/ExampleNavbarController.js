define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var ExampleNavbar = require("./ExampleNavbar");
    var AuthController = require("todo/auth/AuthController");
    var TboneController = require("common/TboneController");

    return TboneController.extend({
        viewClass: ExampleNavbar,

        initialize: function (options) {
            var region = this.options.region;

            this.listenTo(options.model, "change:isLoggedIn", function (model, isLoggedIn) {
                region.$el.toggle(isLoggedIn);
            });
            region._ensureElement();
            region.$el.toggle(options.model.get("isLoggedIn"));
        }
    }, {
        showNavbar: function (region) {
            this._showView(region, AuthController.getCurrentUser());
        }
    });
});