/*global require, document*/
/**
 * RequireJS main function that manually bootstraps the Angular app module.
 *
 * @author Bo Gotthardt
 */
require(["angular", "todoapp/TodoApp"],
    function (angular, TodoApp) {
        "use strict";

        angular.bootstrap(document.body, [TodoApp.name]);
    });