define(function (require) {
    "use strict";
    var _ = require("underscore");
    var Promise = require("common/util/Promise");
    var Associations = require("associations");

    function handleDependencyChange(config, name, scope) {
        // Get each of the required attributes to use as parameters.
        var parameters = config.deps.map(function (dep) {
            return scope.get(dep);
        });

        var value = config.value.apply(scope, parameters);
        scope.set(name, value);
    }

    return Associations.AssociatedModel.extend({
        constructor: function () {
            Associations.AssociatedModel.prototype.constructor.apply(this, arguments);
            var scope = this;

            // Computed attributes.
            if (this.computed) {
                _.each(this.computed, function (config, name) {
                    _.each(config.deps, function (dep) {
                        // Listen for changes and promise resolution for all the required attributes.
                        // Let's hope no-one sets up a circular reference...
                        scope.listenTo(scope, "change:" + dep, function () {
                            handleDependencyChange(config, name, scope);
                        });
                        handleDependencyChange(config, name, scope);
                    });
                });
            }

        },

        sync: function (method, model, options) {
            options.sync = true;
            return Associations.AssociatedModel.prototype.sync.call(this, method, model, options);
        },

        toJSON: function (options) {
            var json = Associations.AssociatedModel.prototype.toJSON.call(this, options);
            if (options && options.sync && this.computed) {
                _.each(this.computed, function (config, name) {
                    delete json[name];
                });
            }
            return json;
        }
    }, {
        fetchById: function (id, fetchArgs) {
            var args = {};
            args[this.prototype.idAttribute] = id;
            var ThisClass = this;
            var model = new ThisClass(args);

            return model.fetch(fetchArgs)
                .then(function () {
                    return model;
                });
        }
    });
});