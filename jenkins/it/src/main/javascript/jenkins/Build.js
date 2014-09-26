define(function (require) {
	"use strict";
	var _ = require("underscore");
	var TboneModel = require("common/TboneModel");
	var Promise = require("bluebird");
	var TestReport = require("jenkins/jenkins/TestReport");
    var Logger = require("common/util/Logger");

    var log = new Logger("Build");

	/**
	 * A build of a specific Jenkins job.
	 *
	 * @class Build
	 */
	return TboneModel.extend({
		defaults: {
			building: false,
			fullDisplayName: null,
			result: null,
			url: null,
			job: null
		},

		idAttribute: "number",

		url: function () {
			return "/job/" + this.get("job.name") + "/" + this.get("number") + "/api/json";
		},

		/**
		 * Whether the build completed successfully.
		 * @returns {Boolean}
		 */
		isSuccessful: function () {
			return this.get("result") === "SUCCESS";
		},

		/**
		 * Get the parameters used to trigger the build.
		 * @returns {Object}
		 */
		getTriggerParameters: function () {
			var parametersAction = _.find(this.get("actions"), function (action) {
				return action.parameters;
			});

			return parametersAction.parameters;
		},

		/**
		 * Get the username of the user that triggered the build.
		 * @returns {String}
		 */
		getTriggerUser: function () {
			var causesAction = _.find(this.get("actions"), function (action) {
				return action.causes;
			});

			return causesAction.causes.userName;
		},

		/**
		 * Get the test report generated for this build.
		 * @returns {Promise} A promise for the test report.
		 */
		fetchTestReport: function () {
			return TestReport.fetch(null, {
				build: this
			});
		},

		/**
		 * Wait for the build to succeed.
		 * @returns {Promise} A promise for the build having completed successfully.
		 */
		success: function () {
			var scope = this;
			return this.fetch()
				.then(function () {
					if (scope.get("building")) {
                        log.info("Build in progress, refreshing in 30 seconds...");
						return Promise.delay(30000)
							.then(function () {
								return scope.success();
							});
					} else {
						if (scope.isSuccessful()) {
                            log.info("Build successful.");
							return scope;
						} else {
                            log.info("Build failed.");
							return Promise.reject();
						}
					}
				});
		},

		/**
		 * Trigger a new build with the same parameters.
		 * @returns {Promise} A promise for the started build.
		 */
		rebuild: function () {
			return this.get("job").triggerBuild(this.getTriggerParameters());
		}
	});
});
