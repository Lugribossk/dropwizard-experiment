define(["angular"],
    function (angular) {
        "use strict";

        var app = angular.module("todoapp", []);

        app.directive("todoapp", function () {
            return {
                restrict: "E",
                replace: true,
                template: "<div>Hello world</div>"
            };
        })

        return app;
    });