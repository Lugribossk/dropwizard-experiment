define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var ExampleNavbar = require("./ExampleNavbar");
    var LoginController = require("user/LoginController");
    var TboneController = require("tbone/TboneController");

    return TboneController.extend({
        viewType: ExampleNavbar,

        initialize: function (options) {
            var region = this.options.region;

            this.listenTo(options.model, "change:isLoggedIn", function (model, isLoggedIn) {
                region.$el.toggle(isLoggedIn);
            });
            region.ensureEl();
            region.$el.toggle(options.model.get("isLoggedIn"));
        }
    }, {
        showNavbar: function (region) {
            this._showModel(LoginController.getCurrentUser(), region);
        }
    });
});