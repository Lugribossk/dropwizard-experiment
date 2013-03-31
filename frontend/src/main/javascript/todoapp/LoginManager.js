define(["angular"],
    function (angular) {
        "use strict";

        function LoginManager(scope) {
            this.scope = scope;
            this.loggedIn = false;
        }

        return LoginManager;
    });