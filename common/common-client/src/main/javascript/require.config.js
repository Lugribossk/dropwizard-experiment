/*global require*/
require.config({
    //baseUrl: "javascript",
    paths: {
        backbone: "../../../../../common/common-client/bower_components/backbone/backbone",
        marionette: "../../../../../common/common-client/bower_components/backbone.marionette/lib/backbone.marionette",
        stickit: "../../../../../common/common-client/bower_components/backbone.stickit/backbone.stickit",
        associations: "../../../../../common/common-client/bower_components/backbone-associations/backbone-associations",

        jquery: "../../../../../common/common-client/bower_components/jquery/jquery",
        underscore: "../../../../../common/common-client/bower_components/lodash/dist/lodash",
        Handlebars: "../../../../../common/common-client/bower_components/handlebars/handlebars",

        bootstrap: "../../../../../common/common-client/bower_components/bootstrap/dist/js/bootstrap",

        moment: "../../../../../common/common-client/bower_components/moment/moment",
        md5: "../../../../../common/common-client/bower_components/JavaScript-MD5/js/md5",
        ladda: "../../../../../common/common-client/bower_components/Ladda/js/ladda",
        spin: "../../../../../common/common-client/bower_components/Ladda/js/spin",

        text: "../../../../../common/common-client/bower_components/requirejs-text/text",
        hbars: "../../../../../common/common-client/bower_components/requirejs-handlebars/hbars",
        less: "../../../../../common/common-client/bower_components/require-less/less",
        lessc: "../../../../../common/common-client/bower_components/require-less/lessc",
        normalize: "../../../../../common/common-client/bower_components/require-less/normalize",
        "less-builder": "../../../../../common/common-client/bower_components/require-less/less-builder",

        tbone: "../../../../../common/common-client/src/main/javascript",

        test: "src/test/javascript",
        "jasmine-jquery": "../../../../../common/common-client/bower_components/jasmine-jquery/lib/jasmine-jquery",
        "jasmine-as-promised": "../../../../../common/common-client/bower_components/jasmine-as-promised/src/jasmine-as-promised",
        "es5-shim": "../../../../../common/common-client/bower_components/es5-shim/es5-shim"
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