define(["angular", "todoapp/TodoAppInternal", "todoapp/LoginManager", "todoapp/MainView", "login/LoginView"],
    function (angular, module, LoginManager, MainView, LoginView) {
        "use strict";

        function todoAppController(scope, loginManager) {
            scope.loginManager = loginManager;
        }

        todoAppController.$inject = ["$scope", "loginManager"];


        module.directive("todoapp", function () {
            return {
                restrict: "E",
                replace: true,
                controller: todoAppController,
                template:   "<div>" +
                                "<div ng-switch='loginManager.loggedIn'>" +
                                    "<mainview ng-switch-when='true'/>" +
                                    "<login ng-switch-default/>" +
                                "</div>" +
                            "</div>"
            };
        });

        module.factory("loginManager", ["$rootScope", function (scope) {
            return new LoginManager(scope);
        }]);

        return module;
    });