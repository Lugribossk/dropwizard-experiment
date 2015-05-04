import Router from "./Router";

/**
 * Router that blocks non-public routes if there is no current user.
 */
export default class AuthenticatingRouter extends Router {
    /**
     * @param {Window} window
     * @param {CurrentUserStore} userStore
     */
    constructor(window, userStore) {
        super(window);
        this.userStore = userStore;
        this.lastBlocked = null;

        userStore.onUserChange(this.onUserChange.bind(this));
    }

    foundMatchingRoute(data) {
        if (!this.userStore.getUser() && !data.isPublic) {
            this.lastBlocked = data;
        } else {
            super.foundMatchingRoute(data);
        }
    }

    /**
     * Add a hander for the specified public route.
     * @param {String} url
     * @param {Function} handler
     */
    addPublic(url, handler) {
        this.routes.push({
            regex: Router.buildRegex(url),
            handler: handler,
            isPublic: true
        });
    }

    onUserChange(user) {
        if (user && this.lastBlocked) {
            this.foundMatchingRoute(this.lastBlocked);
            this.lastBlocked = null;
        }
    }
}