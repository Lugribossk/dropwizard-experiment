/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var Backbone = require("backbone");
    var Gravatar = require("common/ui/Gravatar");
    var TestUtils = require("test/TestUtils");

    describe("Gravatar", function () {
        var dom = TestUtils.createTestDom();

        it("should show Gravatar image.", function () {
            var model = new Backbone.Model({email: "MyEmailAddress@example.com"});

            dom.show(new Gravatar({model: model}));

            expect(dom.find("img").attr("src")).toContain("gravatar.com/avatar/0bc83cb571cd1c50ba6f3e8a78ef1346");
        });

        it("should use HTTPS.", function () {
            var model = new Backbone.Model({email: "MyEmailAddress@example.com"});

            dom.show(new Gravatar({model: model}));

            expect(dom.find("img").attr("src")).toContain("https://secure.gravatar.com");
        });
    });
});