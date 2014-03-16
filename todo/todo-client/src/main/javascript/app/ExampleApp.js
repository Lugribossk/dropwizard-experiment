define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Logger = require("tbone/util/Logger");
    var ExampleNavbarController = require("navbar/ExampleNavbarController");
    var ExampleRouter = require("app/ExampleRouter");
    var AuthenticatingHistory = require("auth/AuthenticatingHistory");
    var DeferredRegion = require("tbone/view/DeferredRegion");
    require("less!./ExampleApp");

    var app = new Marionette.Application();

    app.addRegions({
        content: {
            selector: "#main",
            regionType: DeferredRegion
        },
        navbar: {
            selector: "#navbar",
            regionType: DeferredRegion
        }
    });

    app.addInitializer(Logger.initialize);

    app.addInitializer(function () {
        AuthenticatingHistory.initialize();

        var router = new ExampleRouter({region: this.content});

        // These are actually options for AuthenticatingHistory.
        Backbone.history.start({
            region: this.content
        });
    });

    app.addInitializer(function () {
        ExampleNavbarController.showNavbar(this.navbar);
    });

    return app;
});