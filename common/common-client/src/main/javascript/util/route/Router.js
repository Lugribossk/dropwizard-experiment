import _ from "lodash";

/**
 * Hash fragment navigation router.
 * There are many router libraries out there, but none of them supported all the features used here.
 */
export default class Router {
    /**
     * @param {Window} window
     */
    constructor(window) {
        this.routes = [];
        this.listeners = [];
        this.hash = "";
        this.content = "";
        this.window = window;
    }

    /**
     * Listen for a new route being matched.
     * @param listener
     */
    onRouteChange(listener) {
        this.listeners.push(listener);
    }

    /**
     * Get the current route url (i.e. the hash fragment).
     * @returns {String}
     */
    getRouteUrl() {
        return this.hash;
    }

    /**
     * Get the current route content (i.e. the React elements to show).
     * @returns {string|CSSStyleDeclaration.content|*|content|.renderStructuredContent.content|d.Properties.content}
     */
    getRouteContent() {
        return this.content;
    }

    /**
     * Add a handler for the specified route.
     * @param {String|RegExp} url
     * @param {Function} handler
     */
    add(url, handler) {
        this.routes.push({
            regex: Router.buildRegex(url),
            handler: handler
        });
    }

    /**
     * Go to the specified route.
     * @param {String} url
     */
    navigate(url) {
        this.window.location.hash = url;
    }

    /**
     * Begin routing, including routing the current url.
     */
    init() {
        this.window.addEventListener("hashchange", this.handleHashChange.bind(this));

        this.handleHashChange({
            newURL: this.window.location.href
        });
    }

    /**
     * Called whenever a route is matched.
     * @param data
     */
    foundMatchingRoute(data) {
        this.content = data.handler.apply(null, data.args);
        this.hash = data.hash;

        _.forEach(this.listeners, (listener) => {
            listener(this.content, this.hash);
        });
    }

    /**
     * Called whenever a route is not matched.
     * @param hash
     */
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