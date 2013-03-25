/**
 * Directive for creating Bootstrap icons.
 *
 * E.g. <icon map-marker/> for the icon named "icon-map-marker".
 *
 * @author Bo Gotthardt
 */
define(["angular", "bootstrap/BootstrapInternal"],
    function (angular, module) {
        "use strict";

        /**
         * Convert the specified string from camelCase to snake-case.
         *
         * @param {String} camelCase
         * @return {String}
         */
        function toSnakeCase(camelCase) {
            return camelCase.replace(/([A-Z])/, function (capture) {
                return "-" + capture.toLocaleLowerCase();
            });
        }

        module.directive("icon", function () {
            return {
                restrict: "E",
                compile: function (element, attrs) {
                    var iconElement = angular.element("<i/>");

                    angular.forEach(attrs, function (value, attr) {
                        if (attr === "class") {
                            iconElement.addClass(value);
                        } else if (value === "") {
                            iconElement.addClass("icon-" + toSnakeCase(attr));
                        }
                    });

                    element.replaceWith(iconElement);
                }
            };
        });

        return module;
    });