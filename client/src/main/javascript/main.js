/*global require, document*/
require(["angular", "todoapp/todoapp"],
    function (angular, todoapp) {
        "use strict";

        angular.bootstrap(document.body, [todoapp.name]);
    });