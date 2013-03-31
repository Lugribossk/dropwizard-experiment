define(["angular", "todoapp/TodoAppInternal"],
    function (angular, module) {
        "use strict";

        function loginController(scope, loginManager) {
            scope.blah = function () {
                loginManager.loggedIn = true;
            };
        }

        loginController.$inject = ["$scope", "loginManager"];


        module.directive("login", function () {
            return {
                restrict: "E",
                replace: true,
                controller: loginController,
                template: "<div ng-click='blah()'>Login</div>"
            };
        });
    });