define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var Associations = require("associations");
	var TboneModel = require("common/TboneModel");
	var LogItem = require("jenkins/runner/LogItem");
	var Job = require("jenkins/jenkins/Job");
    var Logger = require("common/util/Logger");

    var log = new Logger("MergeAttempt");

	/**
	 * An attempt at merging a specific set of repository branches to master by running integration tests for them.
	 *
	 * @class MergeAttempt
	 */
	return TboneModel.extend({
		defaults: {
			repoBranches: null,
			changelog: null
		},

		relations: [{
			type: Associations.Many,
			key: "logItems",
			relatedModel: LogItem
		}],

        initialize: function () {
            this.set("logItems", new Backbone.Collection());
        },

		/**
		 * Start integration tests.
		 * @returns {Promise}
		 */
		startITs: function () {
			var scope = this;
			var job = new Job({name: "integration-test-generic-build"});
			var build = job.triggerBuild(this.get("repoBranches"));

            this._log("itwait", "Waiting for ITs to start.");

			return build
                .then(function (build) {
                    scope._log("itstart", "ITs started.");

                    return build.success();
                })
				.then(function () {
					scope._log("itsuccess", "ITs passed succesfully!");

					return scope.merge();
				}, function (error) {
                    log.info("IT build failed with error", error);
					return scope._handleError(error);
				});
		},

		/**
		 * Merge branches to master.
		 * @returns {Promise} A promise for the merge having succeeded.
		 */
		merge: function () {
            var scope = this;
			var job = new Job({name: "pull-request"});
			var params = _.extend({
				CHANGELOG: this.get("changelog"),
				CHANGELEVEL: "Level 1: (least impact) Functional defect resolution; some performance improvements"
			}, this.get("repoBranches"));

            scope._log("mergewait", "Waiting for merge to start.");

			return job.triggerBuild(params)
                .then(function (build) {
                    scope._log("mergestart", "Merge started");

                    return build.success();
                })
                .then(function () {
                    scope._log("mergesuccess", "Merge successful!");
                }, function () {
                    scope._log("mergefail", "Merge failed.");
                });
		},

		_log: function (type, message, data) {
			var params = _.extend({
				type: type,
				message: message
			}, data);
			this.get("logItems").add(new LogItem(params));
            log.info(message);
		},

		_handleError: function (error) {

		}
	});
});
