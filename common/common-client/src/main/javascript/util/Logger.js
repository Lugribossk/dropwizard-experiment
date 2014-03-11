/*global console*/
define(function (require) {
    "use strict";
    var _ = require("underscore");

    var defaultLogLevel = 2,
        specificLogLevels = {},
        loggers = {},
        levels = {
            TRACE: 1,
            DEBUG: 2,
            INFO: 3,
            WARN: 4,
            ERROR: 5
        };

    function noop() {}

    /**
     * Named logger that can have some or all of its output disabled.
     * Log output is listed as coming directly from the calling code, not a wrapper (inspired by http://pimterry.github.io/loglevel/).
     *
     * @class Logger
     * @constructor
     *
     * @param {String} name
     */
    function Logger(name) {
        if (loggers[name]) {
            return loggers[name];
        }
        this.name = name;
        this.setLogLevel(specificLogLevels[name] || defaultLogLevel);
        loggers[name] = this;
    }

    Logger.prototype.setLogLevel = function (level) {
        var scope = this;
        // Add the logging methods if the log level is high enough, otherwise a function that does nothing.
        // Binding their context to console ensures that they work just like calling directly on console, including correct line number reference.
        _.each(["error", "warn", "info", "debug", "trace"], function (method) {
            if (typeof console !== "undefined" && levels[method.toLocaleUpperCase()] >= level) {
                scope[method] = Function.prototype.bind.call(console[method], console, "[" + scope.name + "]");
            } else {
                scope[method] = noop;
            }
        });

        // Add additional fancy (Chrome-only?) console methods.
        _.each(["group", "groupEnd", "groupCollapsed"], function (method) {
            if (typeof console !== "undefined" && console[method]) {
                scope[method] = Function.prototype.bind.call(console[method], console, "[" + scope.name + "]");
            } else {
                scope[method] = noop;
            }
        });
    };

    Logger.initialize = function (config) {
        if (config.Logger) {
            _.each(config.Logger, function (value, key) {
                if (key === "logLevel") {
                    Logger.setAllLogLevels(value);
                } else {
                    Logger.setLogLevel(key, value);
                }
            });
        }
    };

    Logger.setAllLogLevels = function (level) {
        defaultLogLevel = level;
        _.each(loggers, function (logger) {
            logger.setLogLevel(level);
        });
    };

    Logger.setLogLevel = function (name, level) {
        specificLogLevels[name] = level;
        var logger = loggers[name];
        if (logger) {
            logger.setLogLevel(level);
        }
    };

    Logger.LogLevel = levels;

    return Logger;
});