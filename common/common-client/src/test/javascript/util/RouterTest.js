import expect from "expect.js";
import sinon from "sinon";
import Router from "../../../main/javascript/util/route/Router";

describe("Router", () => {
    var mockWindow;
    var hashChangeHandler;
    beforeEach(() => {
        mockWindow = {
            addEventListener: (event, handler) => {
                hashChangeHandler = handler;
            },
            location: {
                href: ""
            }
        }
    });

    it("should call handler for matching route.", () => {
        var handler = sinon.spy();
        var router = new Router(mockWindow);
        router.add("test", handler);
        router.init();

        hashChangeHandler({newURL: "#test"});

        expect(handler.called).to.be.ok();
    });
});