define(["angular", "todoapp/TodoAppInternal"],
    function (angular, module) {
        "use strict";

        module.directive("mainview", function () {
            return {
                restrict: "E",
                replace: true,
                template: "<div>Hello World</div>"
            };
        });

        return module;
    });