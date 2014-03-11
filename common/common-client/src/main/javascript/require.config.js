/*global require*/
require.config({
    baseUrl: "javascript",
    paths: {
        backbone: "../../bower_components/backbone/backbone",
        marionette: "../../bower_components/backbone.marionette/lib/backbone.marionette",
        stickit: "../../bower_components/backbone.stickit/backbone.stickit",
        associations: "../../bower_components/backbone-associations/backbone-associations",

        jquery: "../../bower_components/jquery/jquery",
        underscore: "../../bower_components/lodash/dist/lodash",
        Handlebars: "../../bower_components/handlebars/handlebars",

        bootstrap: "../../bower_components/bootstrap/dist/js/bootstrap",

        moment: "../../bower_components/moment/moment",
        md5: "../../bower_components/JavaScript-MD5/js/md5",
        ladda: "../../bower_components/Ladda/js/ladda",
        spin: "../../bower_components/Ladda/js/spin",

        text: "../../bower_components/requirejs-text/text",
        hbars: "../../bower_components/requirejs-handlebars/hbars",
        less: "../../bower_components/require-less/less",
        lessc: "../../bower_components/require-less/lessc",
        normalize: "../../bower_components/require-less/normalize",
        "less-builder": "../../bower_components/require-less/less-builder",

        test: "../../test/javascript",
        "jasmine-jquery": "../../bower_components/jasmine-jquery/lib/jasmine-jquery",
        "jasmine-as-promised": "../../bower_components/jasmine-as-promised/src/jasmine-as-promised",
        "es5-shim": "../../bower_components/es5-shim/es5-shim"
    },
    shim: {
        backbone: {
            deps: ["jquery", "underscore"],
            exports: "Backbone"
        },
        marionette: {
            deps: ["backbone", "underscore"],
            exports: "Marionette"
        },
        stickit: {
            deps: ["backbone", "jquery"],
            exports: "Backbone.Stickit"
        },
        associations: {
            deps: ["backbone", "underscore"],
            exports: "Backbone.Associations"
        },
        Handlebars: {
            exports: "Handlebars"
        },
        bootstrap: {
            deps: ["jquery"]
        },

        "jasmine-jquery": {
            deps: ["jquery"]
        }
    },
    hbars: {
        extension: ".hbs"
    }
});