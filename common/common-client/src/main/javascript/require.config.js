/*global require*/
require.config({
    baseUrl: "../../../../../",
    paths: {
        "real-backbone": "common/common-client/bower_components/backbone/backbone",
        "real-marionette": "common/common-client/bower_components/backbone.marionette/lib/backbone.marionette",
        backbone: "common/common-client/src/main/javascript/shim/backbone",
        marionette: "common/common-client/src/main/javascript/shim/marionette",

        stickit: "common/common-client/bower_components/backbone.stickit/backbone.stickit",
        associations: "common/common-client/bower_components/backbone-associations/backbone-associations",

        jquery: "common/common-client/bower_components/jquery/dist/jquery",
        underscore: "common/common-client/bower_components/lodash/dist/lodash",
        Handlebars: "common/common-client/bower_components/handlebars/handlebars",

        bootstrap: "common/common-client/bower_components/bootstrap/dist/js/bootstrap",

        moment: "common/common-client/bower_components/moment/moment",
        md5: "common/common-client/bower_components/JavaScript-MD5/js/md5",
        ladda: "common/common-client/bower_components/Ladda/js/ladda",
        spin: "common/common-client/bower_components/Ladda/js/spin",
        bluebird: "common/common-client/bower_components/bluebird/js/browser/bluebird",

        text: "common/common-client/bower_components/requirejs-text/text",
        hbars: "common/common-client/bower_components/requirejs-handlebars/hbars",
        less: "common/common-client/bower_components/require-less/less",
        lessc: "common/common-client/bower_components/require-less/lessc",
        normalize: "common/common-client/bower_components/require-less/normalize",
        "less-builder": "common/common-client/bower_components/require-less/less-builder",

        common: "common/common-client/src/main/javascript",
        todo: "todo/todo-client/src/main/javascript",
        dashboard: "build/dashboard/src/main/javascript",

        test: "common/common-client/src/test/javascript",
        "jasmine-jquery": "common/common-client/bower_components/jasmine-jquery/lib/jasmine-jquery",
        "es5-shim": "common/common-client/bower_components/es5-shim/es5-shim"
    },
    shim: {
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