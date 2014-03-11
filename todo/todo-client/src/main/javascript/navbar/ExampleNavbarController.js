define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var ExampleNavbar = require("./ExampleNavbar");

    return Marionette.Controller.extend({
        initialize: function () {
            var currentUser = this.options.currentUser;
            var region = this.options.region;

            this.listenTo(currentUser, "change:isLoggedIn", function (model, isLoggedIn) {
                region.$el.toggle(isLoggedIn);
            });
            region.ensureEl();
            region.$el.toggle(currentUser.get("isLoggedIn"));
        },

        showNavbar: function () {
            this.options.region.show(new ExampleNavbar({model: this.options.currentUser}));
        }
    });
});