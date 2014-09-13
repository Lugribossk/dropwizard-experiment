/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var Highlighter = require("common/util/Highlighter");

    describe("Highlighter", function () {
        describe("highlight()", function () {
            var h;
            beforeEach(function () {
                h = new Highlighter("(", ")");
            });
            it("should highlight single letters.", function () {
                expect(h.highlight("abcd", "a")).toBe("(a)bcd");
                expect(h.highlight("abcd", "b")).toBe("a(b)cd");
                expect(h.highlight("abcd", "d")).toBe("abc(d)");
            });

            it("should highlight multiple letters.", function () {
                expect(h.highlight("abcd", "ab")).toBe("(ab)cd");
                expect(h.highlight("abcd", "bc")).toBe("a(bc)d");
                expect(h.highlight("abcd", "cd")).toBe("ab(cd)");
            });

            it("should highlight multiple of the same letter.", function () {
                expect(h.highlight("abcda", "a")).toBe("(a)bcd(a)");
                expect(h.highlight("abbcd", "b")).toBe("a(b)(b)cd");
            });

            it("should escape HTML in input.", function () {
                expect(h.highlight("a<b>b</b>cd", "a")).toBe("(a)&lt;b&gt;b&lt;/b&gt;cd");
                expect(h.highlight("a<b>b</b>cd", "d")).toBe("a&lt;b&gt;b&lt;/b&gt;c(d)");
            });
        });
    });
});