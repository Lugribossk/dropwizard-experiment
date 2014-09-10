define(function (require) {
    "use strict";

    return {
        getBaseUrl: function () {
            if (window.location.host === "localhost:9090") {
                // When developing the code and the API are served from different processes on different ports.
                return "http://localhost:8080/api";
            } else {
                // But after building they are served from one location.
                return window.location.protocol + "//" + window.location.host + "/api";
            }
        }
    };
});
