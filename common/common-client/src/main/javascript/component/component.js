define(function (require) {
    "use strict";
    var Logger = require("tbone/util/Logger");

    var log = new Logger("ComponentPlugin");
    var loadedComponents = {};

    /**
     * RequireJS plugin for loading components, separate layers (i.e. file bundles) with multiple modules in them.
     * Using the plugin to require something in another component will then trigger loading of the entire layer.
     * This is useful for bundling a large app into several layers that can then be loaded only when needed, while
     * still being able to refer freely to any module in the bundle.
     */
    return {
        load: function (name, req, onload, config) {
            // When building, do nothing as the optimizer config will ensure the creation of layers for each component.
            if (config.isBuild) {
                onload();
                return;
            }

            var componentName = name.substring(0, name.indexOf("/"));
            function loadModule() {
                if (!loadedComponents[componentName]) {
                    loadedComponents[componentName] = true;
                    log.info("Loaded component", componentName, "due to requirement for", name);
                }
                req([name], function (value) {
                    onload(value);
                });
            }

            // When in development mode, load the file directly.
            // The config value will be set by the optimizer config somehow. TODO
            if (!config.optimized) {
                loadModule();
                return;
            }

            // When optimized, load the layer with the component as it defines all of that component's modules.
            req([componentName], function () {
                loadModule();
            });
        }
    };
});