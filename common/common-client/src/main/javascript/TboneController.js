define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");

    return Marionette.Controller.extend({
        constructor: function () {
            Marionette.Controller.prototype.constructor.apply(this, arguments);

        }

        /*_showModel: function (modelPromise, viewOptions) {
            var scope = this;
            this.options.region.show(modelPromise
                .then(function (model) {
                    scope.model = model;

                    var ViewClass = scope.viewType;
                    var view = new ViewClass(_.extend({
                        controller: scope,
                        model: model
                    }, viewOptions));
                    scope.listenTo(view, "close", this.close);

                    return view;
                }));
        }*/
    }, {
        _showModel: function (modelPromise, region, viewOptions) {
            var ThisClass = this;

            region.show($.when(modelPromise)
                .then(function (model) {
                    var controller = new ThisClass({
                        model: model
                    });

                    var ViewClass = controller.viewType;
                    var view = new ViewClass(_.extend({
                        controller: controller,
                        model: model
                    }, viewOptions));
                    controller.listenTo(view, "close", controller.close);

                    return view;
                }));
        }
    });
});