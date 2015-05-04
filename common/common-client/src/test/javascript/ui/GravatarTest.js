import expect from "expect.js";
import Gravatar from "../../../main/javascript/ui/Gravatar";
import React from "react/addons";
var TestUtils = React.addons.TestUtils;

describe("Gravatar", () => {
    var gravatar, img;
    beforeEach(() => {
        gravatar = TestUtils.renderIntoDocument(
            <Gravatar email="MyEmailAddress@example.com" size="40"/>
        );
        img = React.findDOMNode(gravatar);
    });

    it("should show Gravatar image.", () => {
        expect(img.src).to.contain("gravatar.com/avatar/0bc83cb571cd1c50ba6f3e8a78ef1346");
    });

    it("should use HTTPS.", () => {
        expect(img.src).to.contain("https://secure.gravatar.com");
    });
});