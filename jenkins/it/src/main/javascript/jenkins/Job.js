define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var TboneModel = require("common/TboneModel");
	var Associations = require("associations");
	var Build = require("jenkins/jenkins/Build");
    var Queue = require("jenkins/jenkins/Queue");
    var Logger = require("common/util/Logger");

    var log = new Logger("Job");

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

		triggerBuild: function (data) {
			var scope = this;
			var parameters = [];
			_.each(data, function (value, name) {
				parameters.push({
					name: name,
					value: value
				});
			});

            log.info("Triggering build with", data);

			return this.save(null, {
				url: "/job/" + this.get("name") + "/buildWithParameters",
				type: "POST",
				data: {
					parameter: parameters
				}
			}).then(function () {
                log.error("Unexpected queue behavior.");
			}, function (xhr) {
                log.info("Build queued.");
                // As usual, a 2xx response with no body is an error in jQuery-land...
                var queueUrl = xhr.getResponseHeader("Location");
                return Queue.fromUrl(queueUrl).success();
            }).then(function (queue) {
                log.info("Queueing complete, build started.");
				return new Build({
					job: scope,
					number: queue.getBuildNumber(),
					triggerParameters: data
				});
			}, function () {
                log.error("TODO");
			});
		}
	});
});
