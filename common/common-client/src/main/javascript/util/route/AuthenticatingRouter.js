import Router from "./Router";

export default class AuthenticatingRouter extends Router {
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