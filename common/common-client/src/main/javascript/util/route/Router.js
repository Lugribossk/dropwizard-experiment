import _ from "lodash";

export default class Router {
    constructor(window) {
        this.routes = [];
        this.listeners = [];
        this.hash = "";
        this.content = "";
        this.window = window;
    }

    onRouteChange(listener) {
        this.listeners.push(listener);
    }

    getRouteUrl() {
        return this.hash;
    }

    getRouteContent() {
        return this.content;
    }

    add(url, handler) {
        this.routes.push({
            regex: Router.buildRegex(url),
            handler: handler
        });
    }

    navigate(url) {
        this.window.location.hash = url;
    }

    init() {
        this.window.addEventListener("hashchange", this.handleHashChange.bind(this));

        this.handleHashChange({
            newURL: this.window.location.href
        });
    }

    foundMatchingRoute(data) {
        this.content = data.handler.apply(null, data.args);
        this.hash = data.hash;

        _.forEach(this.listeners, (listener) => {
            listener(this.content, this.hash);
        });
    }

    foundNoMatchingRoute(hash) {
        if (hash !== "") {
            // " " lets us change from no hash fragment at all to /# (since the space is somehow trimmed).
            this.navigate(" ");
        }
    }

    handleHashChange(event) {
        var hash = event.newURL.split("#")[1];
        var foundRoute = false;

        _.forEach(this.routes, (config) => {
            var match = config.regex.exec(hash);
            if (match) {
                var data = _.defaults({
                    hash: hash,
                    args: match.slice(1)
                }, config);
                this.foundMatchingRoute(data);
                foundRoute = true;
                return false
            }
        });

        if (!foundRoute) {
            this.foundNoMatchingRoute(hash);
        }
    }

    static buildRegex(url) {
        if (_.isRegExp(url)) {
            return url;
        } else {
            var captureArgs = "^" + url
                .replace(/\\/g, "\\/")
                .replace(/(:\w+)/g, "(\\w+)") + "$";
            return new RegExp(captureArgs);
        }
    }
}