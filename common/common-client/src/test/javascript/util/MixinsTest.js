import expect from "expect.js";
import sinon from "sinon";
import Mixins from "../../../main/javascript/util/Mixins";

describe("Mixins", () => {
    it("should copy methods from mixin to context.", () => {
        var mixin = {
            test: function () {}
        };
        var context = {};

        Mixins.add(context, [mixin]);

        expect(context.test).to.be(mixin.test);
    });

    it("should merge React lifecycle methods with context.", () => {
        var mixin = {
            componentWillUnmount: sinon.spy()
        };
        var originalContextUnmount = sinon.spy();
        var context = {
            componentWillUnmount: originalContextUnmount
        };

        Mixins.add(context, [mixin]);
        context.componentWillUnmount();

        expect(mixin.componentWillUnmount.called).to.be.ok();
        expect(originalContextUnmount).to.be.ok();
    });

    it("should merge React lifecycle methods with multiple mixins.", () => {
        var mixin1 = {
            componentWillUnmount: sinon.spy()
        };
        var mixin2 = {
            componentWillUnmount: sinon.spy()
        };
        var context = {};

        Mixins.add(context, [mixin1, mixin2]);
        context.componentWillUnmount();

        expect(mixin1.componentWillUnmount.called).to.be.ok();
        expect(mixin2.componentWillUnmount.called).to.be.ok();
    });
});