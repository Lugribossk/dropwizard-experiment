define(function (require) {
    "use strict";
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Logger = require("common/util/Logger");
    var ExampleNavbarController = require("todo/navbar/ExampleNavbarController");
    var ExampleRouter = require("todo/app/ExampleRouter");
    var AuthenticatingHistory = require("todo/auth/AuthenticatingHistory");
    var TboneView = require("common/TboneView");
    require("less!./ExampleApp");

    var RootView = TboneView.extend({
        template: false,
        el: "body",
        regions: {
            content: "#main",
            navbar: "#navbar"
        }
    });

    return new Marionette.Application({
        initialize: function () {
            Logger.initialize();

            var rootView = new RootView();
            rootView.render();
            var regions = rootView.getRegions();

            AuthenticatingHistory.initialize({
                region: regions.content
            });

            var router = new ExampleRouter({region: regions.content});

            Backbone.history.start();

            ExampleNavbarController.showNavbar(regions.navbar);
        }
    });
});
