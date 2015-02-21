import CachingStore from "../../../../../../common/common-client/src/main/javascript/flux/CachingStore";
import AuthActions from "./AuthActions";
import Logger from "../../../../../../common/common-client/src/main/javascript/util/Logger";
import User from "./User";
import OAuth2AccessToken from "./OAuth2AccessToken";

var log = new Logger(__filename);

export default class CurrentUserStore extends CachingStore {
    constructor(api) {
        super(__filename);
        this.api = api;

        this.state = this.getCachedState() || {
            user: null,
            accessToken: null
        };

        if (this.state.accessToken) {
            this.api.authenticateWith(this.state.accessToken.accessToken);
            // Reload current user in case the token has expired.
            this._fetchCurrentUser();
        }

        AuthActions.login.onDispatch(this.login.bind(this));
        AuthActions.logout.onDispatch(this.logout.bind(this));

        this._registerListener("accessToken", () => {
            // TODO have api listen for token changes? But that results in a circular dependency
            if (this.state.accessToken) {
                this.api.authenticateWith(this.state.accessToken.accessToken);
            } else {
                this.api.authenticateWith(null);
            }
        })
    }

    login(username, password) {
        this._fetchAccesstoken(username, password)
            .then(() => {
                this._fetchCurrentUser();
            });
    }

    logout() {
        this.setState({
            user: null,
            accessToken: null
        });
    }

    getUser() {
        return this.state.user;
    }

    onUserChange(listener) {
        return this._registerListener("user", listener);
    }

    onNextSuccessfulLogin(listener) {
        var unsubscribe = this.onUserChange(user => {
            if (user) {
                unsubscribe();
                listener();
            }
        });
    }

    onInvalidLogin(listener) {
        return this._registerListener("invalidLogin", listener);
    }

    unmarshalState(data) {
        return {
            user: data.user ? new User(data.user) : null,
            accessToken: data.accessToken ? new OAuth2AccessToken(data.accessToken) : null
        };
    }

    _fetchAccesstoken(username, password) {
        return this.api.post("/token", {
            username: username,
            password: password,
            grant_type: "password"
        })
            .catch((err) => {
                console.info("Login failed with username", username, err);
                this._trigger("invalidLogin");
            })
            .then((data) => {
                var accessToken = new OAuth2AccessToken(data);
                this.setState({accessToken: accessToken});
                return accessToken;
            });
    }

    _fetchCurrentUser() {
        return this.api.getAs("/users/current", User)
            .then((user) => {
                log.info("Logged in as", user.username);
                this.setState({user: user});
            }).catch((err) => {
                log.error("Unable to get current user:", err);
                this.setState({
                    user: null,
                    accessToken: null
                });
            });
    }
}