define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var Promise = require("bluebird");

    return Marionette.Controller.extend({
        constructor: function (options) {
            Marionette.Controller.prototype.constructor.call(this, options);

            if (options.model) {
                this.model = options.model;
            }
            if (options.collection) {
                this.collection = options.collection;
            }

            Marionette.bindEntityEvents(this, this.model, Marionette.getOption(this, "modelEvents"));
            Marionette.bindEntityEvents(this, this.collection, Marionette.getOption(this, "collectionEvents"));
        }
    }, {
        _showView: function (region, modelPromise, viewClass, viewOptions) {
            var ThisClass = this;

            return region.show(Promise.resolve(modelPromise)
                .then(function (model) {
                    var controllerArgs = {
                        region: region
                    };

                    if (model) {
                        controllerArgs.model = model;
                    }
                    var controller = new ThisClass(controllerArgs);

                    var ViewClass = controller.viewClass || viewClass;
                    var viewArgs = {
                        controller: controller
                    };
                    if (model) {
                        viewArgs.model = model;
                    }
                    var view = new ViewClass(_.extend(viewArgs, viewOptions || {}));
                    controller.listenTo(view, "destroy", controller.destroy);
                    Marionette.bindEntityEvents(controller, view, Marionette.getOption(controller, "events"));

                    return view;
                }));
        }
    });
});