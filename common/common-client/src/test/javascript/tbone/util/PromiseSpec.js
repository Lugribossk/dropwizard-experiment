/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var $ = require("jquery");
    var Promise = require("common/util/Promise");

    describe("Promise", function () {
        it("rejected() should return a rejected promise", function (done) {
            var promise = Promise.rejected("a", "b");

            expect(promise.state()).toBe("rejected");
            promise.always(function (a, b) {
                expect(a).toBe("a");
                expect(b).toBe("b");
                done();
            });
        });

        it("resolved() should return a resolved promise", function (done) {
            var promise = Promise.resolved("a", "b");

            expect(promise.state()).toBe("resolved");
            promise.always(function (a, b) {
                expect(a).toBe("a");
                expect(b).toBe("b");
                done();
            });
        });

        describe("all()", function () {
            it("should resolve with the values of its arguments", function (done) {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var all = Promise.all([def1, def2, "c"]);

                def1.resolve("a");
                def2.resolve("b");

                expect(all.state()).toBe("resolved");
                all.always(function (abc) {
                    expect(abc).toEqual(["a", "b", "c"]);
                    done();
                });
            });

            it("should progress as its arguments resolve", function (done) {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var all = Promise.all([def1, def2, "c"]);

                var step = 0;
                all.progress(function (value, percent) {
                    step++;
                    expect(percent).toBe(step / 3);
                });

                def1.resolve("a");
                def2.resolve("b");

                all.always(function () {
                    expect(step).toBe(3);
                    done();
                });
            });

            it("should reject if one of its arguments reject", function (done) {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var all = Promise.all([def1, def2, "c"]);

                def1.resolve("a");
                def2.reject();

                expect(all.state()).toBe("rejected");
                all.always(function () {
                    expect(true).toBe(true);
                    done();
                });
            });
        });

        describe("any()", function () {
            it("should resolve with the value of any of its arguments that resolve", function (done) {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var any = Promise.any([def1, def2, "c"]);

                def1.resolve("a");
                def2.reject();

                expect(any.state()).toBe("resolved");
                any.always(function (ac) {
                    expect(ac).toEqual(["a", "c"]);
                    done();
                });
            });

            it("should progress as its arguments resolve", function (done) {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var any = Promise.any([def1, def2, "c"]);

                var step = 0;
                any.progress(function (value, percent) {
                    step++;
                    expect(percent).toBe(step / 3);
                });

                def1.reject();
                def2.resolve("b");

                any.always(function () {
                    expect(step).toBe(2);
                    done();
                });
            });

            it("should not reject even if all its arguments reject", function (done) {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var any = Promise.any([def1, def2]);

                def1.reject();
                def2.reject();

                expect(any.state()).toBe("resolved");
                any.always(function (values) {
                    expect(values).toEqual([]);
                    done();
                });
            });
        });

        describe("isPromise()", function () {
            it("should detect jQuery Deferreds", function () {
                var def = new $.Deferred();
                expect(Promise.isPromise(def)).toBe(true);
            });

            it("should detect jQuery promises", function () {
                var def = new $.Deferred();
                expect(Promise.isPromise(def.promise())).toBe(true);
            });

            it("should not detect other objects", function () {
                expect(Promise.isPromise("promise")).toBe(false);
                expect(Promise.isPromise(function () {})).toBe(false);
                expect(Promise.isPromise({
                    promise: true
                })).toBe(false);
            });
        });
    });
});