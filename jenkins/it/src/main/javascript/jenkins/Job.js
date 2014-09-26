define(function (require) {
	"use strict";
	var _ = require("underscore");
	var TboneModel = require("common/TboneModel");
	var Associations = require("associations");
	var Build = require("jenkins/jenkins/Build");
    var Queue = require("jenkins/jenkins/Queue");
    var Logger = require("common/util/Logger");
	var Promise = require("bluebird");

    var log = new Logger("Job");

	/**
	 * A Jenkins job.
	 *
	 * @class Job
	 */
	return TboneModel.extend({
		defaults: {
			name: null,
			nextBuildNumber: null
		},

		relations: [{
			type: Associations.Many,
			key: "builds",
			relatedModel: Build
		}, {
			type: Associations.One,
			key: "lastBuild",
			relatedModel: Build
		}],

		idAttribute: "name",

		url: function () {
			return "/job/" + this.get("name") + "/api/json";
		},

		/**
		 * Triggers a build of this job with the specified parameters.
		 * @param {Object} data A map of parameter names to their values.
		 * @returns {Promise} A promise for the started build.
		 */
		triggerBuild: function (data) {
			var scope = this;
			var parameters = [];
			_.each(data, function (value, name) {
				if (value.indexOf("origin/") !== 0) {
					value = "origin/" + value;
				}
				parameters.push({
					name: name,
					value: value
				});
			});

            log.info("Triggering", this.get("name"), "build with", data);

			return this.save(null, {
				url: "/job/" + this.get("name") + "/buildWithParameters",
				type: "POST",
				data: {
					parameter: parameters
				}
			}).then(function () {
                log.error("Unexpected queue behavior.");
			}).catch(function (xhr) {
                // As usual, a 2xx response with no body is an error in jQuery-land...
				if (xhr.status === 200) {
	                log.info("Build queued.");

	                var queueUrl = xhr.getResponseHeader("Location");
	                return Queue.fromUrl(queueUrl).success();
				} else {
					return Promise.reject(xhr);
				}
            }).then(function (queue) {
                log.info("Queueing complete, build started.");
				return new Build({
					job: scope,
					number: queue.getBuildNumber(),
					triggerParameters: data
				});
			}).catch(function () {
                log.error("TODO");
			});
		}
	});
});
