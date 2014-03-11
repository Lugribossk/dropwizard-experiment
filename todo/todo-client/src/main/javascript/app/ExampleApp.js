define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Logger = require("tbone/util/Logger");
    var ExampleNavbarController = require("example/navbar/ExampleNavbarController");
    var ExampleRouter = require("example/app/ExampleRouter");
    var ExampleAuthentication = require("example/app/ExampleAuthentication");
    var User = require("example/user/User");
    var LoginController = require("example/user/LoginController");
    require("less!./ExampleApp");

    var app = new Marionette.Application();

    app.addRegions({
        content: "#main",
        navbar: "#navbar"
    });

    app.addInitializer(Logger.initialize);

    app.addInitializer(function () {
        this.currentUser = new User();
        ExampleAuthentication.initialize();

        var router = new ExampleRouter({region: this.content});

        var loginController = new LoginController({
            region: this.content,
            currentUser: this.currentUser
        });

        // These are actually options for ExampleAuthentication.
        Backbone.history.start({
            currentUser: this.currentUser,
            controller: loginController
        });
    });

    app.addInitializer(function () {
        var x = new ExampleNavbarController({
            region: this.navbar,
            currentUser: this.currentUser
        });
        x.showNavbar();
    });

    return app;
});