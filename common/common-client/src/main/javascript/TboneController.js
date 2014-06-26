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
        _showView: function (region, modelPromise, viewType, viewOptions) {
            var ThisClass = this;

            region.show($.when(modelPromise)
                .then(function (model) {
                    var controllerArgs = {
                        region: region
                    };

                    if (model) {
                        controllerArgs.model = model;
                    }
                    var controller = new ThisClass(controllerArgs);

                    var ViewClass = controller.viewType || viewType;
                    var viewArgs = {
                        controller: controller
                    };
                    if (model) {
                        viewArgs.model = model;
                    }
                    var view = new ViewClass(_.extend(viewArgs, viewOptions));
                    controller.listenTo(view, "close", controller.close);

                    return view;
                }));
        }
    });
});