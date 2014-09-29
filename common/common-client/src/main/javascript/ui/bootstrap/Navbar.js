define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneView = require("common/TboneView");
    var Behaviors = require("common/view/Behaviors");

    require("bootstrap");

    Behaviors.registerBehavior("MarkLink", Marionette.Behavior.extend({
        ui: {
            itemLinks: "ul.nav > li > a",
            items: "ul.nav > li"
        },

        links: null,

        initialize: function () {
            this.links = {};
            this.listenTo(Backbone.history, "route", this.markLink);
        },

        onRender: function () {
            var scope = this;
            _.each(this.ui.itemLinks, function (element) {
                var el = $(element);
                var href = el.attr("href");

                // Save links to routes.
                if (href && href.indexOf("#") === 0) {
                    scope.links[href.substring(1)] = el;
                }
            });
            this.markLink();
        },

        markLink: function () {
            var scope = this;
            var route = Backbone.history.fragment;
            _.each(this.links, function (element, link) {
                var isDefault = route === "" && link === "#";
                var isSubpath = route.indexOf(link) === 0;

                if (isDefault || isSubpath) {
                    scope.ui.items.removeClass("active");
                    element.parent().addClass("active");
                }
            });
        }
    }));

    /**
     * Bootstrap navbar.
     * Automatically marks one of its menu items as active if it has a link pointing to the current route.
     *
     * @class Navbar
     */
    return TboneView.extend({
        tagName: "nav",

        className: "navbar navbar-default",

        attributes: {
            role: "navigation"
        },

        behaviors: {
            MarkLink: {}
        }
    });
});