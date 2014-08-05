define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");

    require("stickit");

    return Marionette.LayoutView.extend({
        constructor: function (options) {
            Marionette.LayoutView.prototype.constructor.apply(this, arguments);
            var scope = this;

            if (options && options.controller) {
                this.controller = options.controller;
            }

            if (this.bindings) {
                this.listenTo(this, "render", function () {
                    this.stickit();
                });
            }

            this.listenTo(this, "render", function () {
                setTimeout(function () {
                    scope.triggerMethod("after:render");
                }, 0);
            });
        }
    });
});