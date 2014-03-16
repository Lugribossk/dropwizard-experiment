define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");

    return Marionette.Controller.extend({
        constructor: function (options) {
            Marionette.Controller.prototype.constructor.apply(this, arguments);

            if (options.model) {
                this.model = options.model;
            }
        }
    }, {
        _showModel: function (modelPromise, region, viewOptions) {
            var ThisClass = this;

            region.show($.when(modelPromise)
                .then(function (model) {
                    var controller = new ThisClass({
                        model: model,
                        region: region
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