define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneView = require("tbone/TboneView");

    require("bootstrap");

    return TboneView.extend({
        constructor: function (options) {
            TboneView.prototype.constructor.call(this, options);
            var scope = this;

            var links = {};

            function markLink() {
                var route = Backbone.history.fragment;
                _.each(links, function (element, link) {
                    var isDefault = route === "" && link === "#";
                    var isSubpath = route.length >= link.length && route.substring(0, link.length) === link;

                    if (isDefault || isSubpath) {
                        scope.ui.items.removeClass("active");
                        element.parent().addClass("active");
                    }
                });
            }

            this.listenTo(this, "render", function () {
                _.each(this.ui.itemLinks, function (element) {
                    var el = $(element);
                    var href = el.attr("href");
                    if (href && href.indexOf("#") === 0) {
                        links[href.substring(1)] = el;
                    }
                });
                markLink();
            });

            this.listenTo(Backbone.history, "route", markLink);
        },

        tagName: "nav",

        className: "navbar navbar-default",

        attributes: {
            role: "navigation"
        },

        ui: {
            itemLinks: "ul.nav > li > a",
            items: "ul.nav > li"
        }
    });
});