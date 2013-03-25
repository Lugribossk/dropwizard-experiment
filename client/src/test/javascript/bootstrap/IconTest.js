/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
/**
 * Tests for Icon.
 *
 * @author Bo Gotthardt
 */
define(["angular", "util/DirectiveTester", "bootstrap/Icon"],
    function (angular, DirectiveTester, Icon) {
        "use strict";

        var tester = new DirectiveTester(Icon);

        describe("Icon directive", function () {
            it("should be an i tag.", function () {
                var compiled = tester.compile("<icon star/>");

                expect(compiled.prop("tagName")).toBe("I");
            });

            it("should use the attribute as icon- class.", function () {
                var compiled = tester.compile("<icon star/>");

                expect(compiled).toHaveClass("icon-star");
            });

            it("should pass through class properties.", function () {
                var compiled = tester.compile("<icon star class='blah'/>");

                expect(compiled).toHaveClass("blah");
                expect(compiled).not.toHaveClass("icon-class");
            });
        });
    });