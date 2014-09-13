define(function (require) {
    "use strict";
    var _ = require("underscore");
    var Associations = require("associations");

    function handleDependencyChange(config, name, scope) {
        // Get each of the required attributes to use as parameters.
        var parameters = config.deps.map(function (dep) {
            return scope.get(dep);
        });

        var value = config.value.apply(scope, parameters);
        scope.set(name, value);
    }

    function getBaseUrl() {
        if (window.location.host === "localhost:9090") {
            // When developing the code and the API are served from different processes on different ports.
            return "http://localhost:8080/api";
        } else {
            // But after building they are served from one location.
            return window.location.protocol + "//" + window.location.host + "/api";
        }
    }

    return Associations.AssociatedModel.extend({
        constructor: function () {
            Associations.AssociatedModel.prototype.constructor.apply(this, _.toArray(arguments));
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

        url: function () {
            var url = Associations.AssociatedModel.prototype.url.call(this);
            if (url.indexOf("/") === 0) {
                return getBaseUrl() + url;
            } else {
                return url;
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
        fetch: function (fetchArgs, modelArgs) {
            var ThisClass = this;
            var model = new ThisClass(modelArgs || {});

            return model.fetch(fetchArgs)
                .then(function () {
                    return model;
                });
        },

        fetchById: function (id) {
            var args = {};
            args[this.prototype.idAttribute] = id;

            return this.fetch(null, args);
        },

        getBaseUrl: getBaseUrl
    });
});
