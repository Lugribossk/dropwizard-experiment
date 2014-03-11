define(function (require) {
    "use strict";

    var configuration;

    function Configuration(name) {
        this.name = name;
    }

    Configuration.prototype.get = function (key) {
        if (!key) {
            return configuration[this.name];
        }

        var parts = key.split(".");
        var obj = configuration[this.name];
        while (parts.length > 0) {
            var part = parts.shift();
            if (obj[part]) {
                obj = obj[part];
            } else {
                obj = null;
                break;
            }
        }

        return obj;
    };

    Configuration.initialize = function (config) {
        configuration = config;
    };

    return Configuration;
});