import _ from "lodash";

var loggers = {};

function noop() {}

export default class Logger {
    constructor(name) {
        name = Logger._extractFilename(name);
        if (loggers[name]) {
            return loggers[name];
        }
        this.name = name;
        loggers[name] = this;

        _.forEach(["error", "warn", "info", "debug", "trace"], (method) => {
            // Binding their context to console ensures that they work just like calling directly on console, including correct line number reference.
            // The console methods are apparently not instanceof Function in Node (what are they then...?).
            if (typeof console !== "undefined" && console[method] instanceof Function) {
                this[method] = Function.prototype.bind.call(console[method], console, "[" + this.name + "]");
            } else {
                this[method] = noop;
            }
        });
    }

    static _extractFilename(filename) {
        if (_.endsWith(filename, ".js")) {
            var sep;
            if (_.contains(filename, "/")) {
                sep = "/";
            } else {
                sep = "\\";
            }
            var start = filename.lastIndexOf(sep) + 1;
            var end = filename.lastIndexOf(".");
            return filename.substr(start, end - start);
        } else {
            return filename;
        }
    }
}