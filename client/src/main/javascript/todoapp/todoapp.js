define(["angular", "bootstrap/Bootstrap"],
    function (angular, Bootstrap) {
        "use strict";

        var app = angular.module("TodoApp", [Bootstrap.name]);

        app.directive("todoapp", function () {
            return {
                restrict: "E",
                replace: true,
                template: "<div><icon volume-up/> Hello world</div>"
            };
        });

        return app;
    });