define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");

    /**
     * Utility class for Promises/Deferreds. Also defines the Promise class to stop IntelliJ warning about it not being found.
     *
     * The actual Promise/Deferred class is from jQuery, see <a href="http://api.jquery.com/category/deferred-object/">jQuery Deferred documentation</a>.
     *
     * @class Promise
     * @constructor
     */
    function Promise() {}

    /**
     * See <a href="http://api.jquery.com/deferred.then/">deferred.then()</a>.
     *
     * @param {Function} doneFilter
     * @param {Function} [failFilter]
     * @param {Function} [progressFilter]
     */
    Promise.prototype.then = function (doneFilter, failFilter, progressFilter) {};
    /**
     * See <a href="http://api.jquery.com/deferred.done/">deferred.done()</a>.
     *
     * @param {Function} callback
     */
    Promise.prototype.done = function (callback) {};
    /**
     * See <a href="http://api.jquery.com/deferred.fail/">deferred.fail()</a>.
     *
     * @param {Function} callback
     */
    Promise.prototype.fail = function (callback) {};
    /**
     * See <a href="http://api.jquery.com/deferred.progress/">deferred.progress()</a>.
     *
     * @param {Function} callback
     */
    Promise.prototype.progress = function (callback) {};

    /**
     * Create a rejected promise.
     *
     * @static
     *
     * @param {*} [arg]
     * @return {Promise}
     */
    Promise.rejected = function (arg) {
        var deferred = new $.Deferred();
        return deferred.reject.apply(deferred, arguments).promise();
    };

    /**
     * Create a resolved promise.
     *
     * @static
     *
     * @param {*} [arg]
     * @return {Promise}
     */
    Promise.resolved = function (arg) {
        var deferred = new $.Deferred();
        return deferred.resolve.apply(deferred, arguments).promise();
    };


    /**
     * Set up the specified list of subordinate deferreds to progress the combined deferred when done.
     *
     * @param {Deferred[]} subordinates
     * @param {Deferred} combinedDeferred
     */
    function notification(subordinates, combinedDeferred) {
        var numDone = 0;

        _.each(subordinates, function (subordinate) {
            // The subordinates can be both promises and already computed synchronous values.
            // This is the same check as in $.when().
            if (Promise.isPromise(subordinate)) {
                subordinate.done(function (arg) {
                    numDone++;
                    combinedDeferred.notify.call(combinedDeferred, arg, numDone / subordinates.length);
                });
            } else {
                numDone++;
                combinedDeferred.notify.call(combinedDeferred, subordinate, numDone / subordinates.length);
            }
        });

        if (numDone > 0) {
            combinedDeferred.notify(null, numDone / subordinates.length);
        }
    }

    function delay(method, milliseconds, resolveWith, progressEvents) {
        var deferred = new $.Deferred();
        progressEvents = progressEvents || 0;

        if (progressEvents === 0) {
            setTimeout(function () {
                deferred[method](resolveWith);
            }, milliseconds);
        } else {
            var i = 0;

            var handle = setInterval(function () {
                i++;
                deferred.notify(i / progressEvents);
                if (i === progressEvents) {
                    clearInterval(handle);
                    deferred[method](resolveWith);
                }
            }, milliseconds / progressEvents);
        }

        return deferred.promise();
    }

    /**
     * An improved version of $.when() that returns the values as a single list.
     *
     * Also progresses when individual subordinates are done. The progress event will have the resolved Promise's
     * value and the percentage of subordinates that are done as parameters.
     *
     * Note that due to the way progress() behaves, this function has a rather subtle gotcha when one or more of
     * the subordinates are done already (i.e. non-Promises or already resolved Promises).
     * These will then trigger the progress events <b>synchronously</b> while inside this function call. And unlike
     * done() and fail(), progress() handlers attached later are not called with previously triggered events.
     * So it is therefore not guaranteed how many progress events the caller will actually get, unless they create
     * their own deferred, set up a progress handler and only then pass it as the combinedDeferred parameter.
     *
     * @static
     *
     * @param {*[]} subordinates The list of subordinates, either Promises or arbitrary values.
     * @param {Deferred} [combinedDeferred] The "combined" deferred (not Promise) to use, instead of creating it internally.
     * @return {Promise} A promise for the list of the values of all the subordinates.
     *                   The promise interface of combinedDeferred if that was passed.
     */
    Promise.all = function (subordinates, combinedDeferred) {
        // We would like the returned promise to progress whenever an individual promise has resolved, but $.when() does not support that.
        // So we have to create our own deferred that can be resolved by $.when(), and progressed by done() from the individual promises.
        combinedDeferred = combinedDeferred || new $.Deferred();

        notification(subordinates, combinedDeferred);

        $.when.apply(null, subordinates)
            .done(function () {
                // Return the subordinates' values as one list, instead of as individual arguments.
                combinedDeferred.resolve(Array.prototype.slice.call(arguments, 0));
            })
            .fail(combinedDeferred.reject);

        return combinedDeferred.promise();
    };

    /**
     * Alternative version of $.when() that always resolves with a list of the return vales of the subordinates that resolved.
     *
     * Also progresses when individual subordinates are done. The progress event will have the resolved Promise's
     * value and the percentage of subordinates that are done as parameters.
     *
     * See Promise.all() for a note on how the progress events work.
     *
     * @static
     *
     * @param {*[]} subordinates The list of subordinates, either Promises or arbitrary values.
     * @param {Deferred} [combinedDeferred] The "combined" deferred (not Promise) to use, instead of creating it internally.
     * @return {Promise} A promise for a list of the values of the subordinates that resolved.
     */
    Promise.any = function (subordinates, combinedDeferred) {
        combinedDeferred = combinedDeferred || new $.Deferred();

        notification(subordinates, combinedDeferred);

        subordinates = _.map(subordinates, function (subordinate) {
            if (Promise.isPromise(subordinate)) {
                // Always resolve subordinates rather than reject so the when() deferred always resolves.
                return subordinate.then(null, function () {
                    return Promise.resolved();
                });
            } else {
                return subordinate;
            }
        });

        $.when.apply(null, subordinates)
            .done(function () {
                // Return the subordinates as one list, instead of as individual arguments.
                combinedDeferred.resolve(Array.prototype.filter.call(arguments, function (item) {
                    // Filter out empty values caused by the resolution above.
                    return item !== undefined;
                }));
            });

        return combinedDeferred.promise();
    };

    /**
     * Create a promise for a specified number of milliseconds having elapsed.
     *
     * @static
     *
     * @param {Number} milliseconds
     * @param {*} [resolveWith]
     * @param {Number} [progressEvents=0] Fire this number of progress evens in total over the duration of the
     * @returns {Promise}
     */
    Promise.delayResolved = function (milliseconds, resolveWith, progressEvents) {
        return delay("resolve", milliseconds, resolveWith, progressEvents);
    };

    Promise.delayRejected = function (milliseconds, rejectWith, progressEvents) {
        return delay("reject", milliseconds, rejectWith, progressEvents);
    };

    /**
     * Determine whether the specified argument is a Promise.
     *
     * @static
     *
     * @param {*} possiblePromise
     * @return {Boolean}
     */
    Promise.isPromise = function (possiblePromise) {
        // This is how jQuery#when() does it internally.
        return typeof possiblePromise.promise === "function";
    };

    return Promise;
});