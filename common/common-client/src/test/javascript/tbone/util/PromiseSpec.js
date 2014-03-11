/*global describe, it, expect, spyOn, beforeEach, afterEach, jasmine*/
define(function (require) {
    "use strict";
    var $ = require("jquery");
    var Promise = require("tbone/util/Promise");

    describe("Promise", function () {
        it("rejected() should return a rejected promise", function () {
            var promise = Promise.rejected("a", "b");

            expect(promise.state()).toBe("rejected");
            return promise
                .then(null, function (a, b) {
                    expect(a).toBe("a");
                    expect(b).toBe("b");
                });
        });

        it("resolved() should return a resolved promise", function () {
            var promise = Promise.resolved("a", "b");

            expect(promise.state()).toBe("resolved");
            return promise
                .then(function (a, b) {
                    expect(a).toBe("a");
                    expect(b).toBe("b");
                });
        });

        describe("all()", function () {
            it("should resolve with the values of its arguments", function () {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var all = Promise.all([def1, def2, "c"]);

                def1.resolve("a");
                def2.resolve("b");

                expect(all.state()).toBe("resolved");
                return all.then(function (abc) {
                    expect(abc).toEqual(["a", "b", "c"]);
                });
            });

            it("should progress as its arguments resolve", function () {
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

                return all.then(function () {
                    expect(step).toBe(3);
                });
            });

            it("should reject if one of its arguments reject", function () {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var all = Promise.all([def1, def2, "c"]);

                def1.resolve("a");
                def2.reject();

                expect(all.state()).toBe("rejected");
                return all.then(null, function () {
                    expect(true).toBe(true);
                });
            });
        });

        describe("any()", function () {
            it("should resolve with the value of any of its arguments that resolve", function () {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var any = Promise.any([def1, def2, "c"]);

                def1.resolve("a");
                def2.reject();

                expect(any.state()).toBe("resolved");
                return any.then(function (ac) {
                    expect(ac).toEqual(["a", "c"]);
                });
            });

            it("should progress as its arguments resolve", function () {
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

                return any.then(function () {
                    expect(step).toBe(2);
                });
            });

            it("should not reject even if all its arguments reject", function () {
                var def1 = new $.Deferred();
                var def2 = new $.Deferred();
                var any = Promise.any([def1, def2]);

                def1.reject();
                def2.reject();

                expect(any.state()).toBe("resolved");
                return any.then(function (values) {
                    expect(values).toEqual([]);
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