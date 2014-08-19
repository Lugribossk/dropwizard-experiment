define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");

    require("stickit");

    return Marionette.LayoutView.extend({
        constructor: function (options) {
            Marionette.LayoutView.prototype.constructor.call(this, options);
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
                    if (!scope.isDestroyed) {
                        scope.triggerMethod("after:render");
                    }
                }, 0);
            });
        }
    });
});