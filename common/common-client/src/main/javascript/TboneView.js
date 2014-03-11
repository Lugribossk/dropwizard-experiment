define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");

    require("stickit");

    return Marionette.Layout.extend({
        constructor: function (options) {
            Marionette.Layout.prototype.constructor.apply(this, arguments);

            if (options && options.controller) {
                this.controller = options.controller;
            }

            if (this.bindings) {
                this.listenTo(this, "item:rendered", function () {
                    this.stickit();
                });
            }
        }
    });
});