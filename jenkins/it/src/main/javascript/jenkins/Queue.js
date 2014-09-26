define(function (require) {
    "use strict";
    var TboneModel = require("common/TboneModel");
    var Promise = require("bluebird");
    var Logger = require("common/util/Logger");

    var log = new Logger("Queue");

	/**
	 * A triggered build that is queued before starting.
	 *
	 * @class Queue
	 */
    return TboneModel.extend({
        url: function () {
            return "/queue/item/" + this.get("id") + "/api/json";
        },

	    /**
	     * Wait for the build to start.
	     * @returns {Promise} A promise for the build having started.
	     */
        success: function () {
            var scope = this;
            return this.fetch()
                .then(function () {
                    if (scope.get("executable")) {
                        log.info("Queue item executed.");
                        return scope;
                    } else if (scope.get("canceled")) {
                        log.info("Queue item canceled.");
                        return Promise.reject();
                    } else {
                        log.info("Queue item not resolved yet, refreshing in 10 seconds...");
                        return Promise.delay(null, 10000)
                            .then(function () {
                                return scope.success();
                            });
                    }
                });
        },

	    /**
	     * Get the number of the build that was started.
	     * @returns {Number}
	     */
        getBuildNumber: function () {
            return this.get("executable").number;
        }
    }, {
	    /**
	     * Get a Queue instance for the specified url;
	     * @param {String} url
	     * @returns {Queue}
	     */
        fromUrl: function (url) {
            var ThisClass = this;
            var id = /queue\/item\/(\d+)/.exec(url)[1];
            return new ThisClass({id: id});
        }
    });
});
