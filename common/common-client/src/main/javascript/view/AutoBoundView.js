define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");

    return Marionette.ItemView.extend({
        constructor: function () {
            Marionette.ItemView.prototype.constructor.apply(this, arguments);
            var scope = this;

            var autoBind = this.autoBind;
            if (autoBind) {
                this.listenTo(this, "render", function () {
                    var bindings = {};
                    var prefix = _.isString(autoBind) ? autoBind : "#";

                    _.each(scope.model.attributes, function (value, name) {
                        var dashedName = name.replace(/([A-Z])/g, "-$1").toLowerCase();
                        var selector = prefix + dashedName;
                        bindings[selector] = name;
                    });

                    scope.stickit(scope.model, bindings);
                });
            }
        }
    });
});