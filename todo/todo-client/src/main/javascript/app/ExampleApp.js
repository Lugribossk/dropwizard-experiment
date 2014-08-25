define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Logger = require("common/util/Logger");
    var ExampleNavbarController = require("todo/navbar/ExampleNavbarController");
    var ExampleRouter = require("todo/app/ExampleRouter");
    var AuthenticatingHistory = require("todo/auth/AuthenticatingHistory");
    var DeferredRegion = require("common/view/DeferredRegion");
    var Promises = require("common/util/Promises");
    var ExampleApi = require("common/api/ExampleApi");
    require("less!./ExampleApp");

    var app = new Marionette.Application();

    app.addRegions({
        content: {
            selector: "#main",
            regionClass: DeferredRegion
        },
        navbar: {
            selector: "#navbar",
            regionClass: DeferredRegion
        }
    });

    app.addInitializer(Logger.initialize);
    app.addInitializer(Promises.initialize);
    app.addInitializer(ExampleApi.initialize);

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