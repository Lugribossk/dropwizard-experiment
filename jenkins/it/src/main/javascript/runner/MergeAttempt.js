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

		startITs: function () {
			var scope = this;
			var job = new Job({name: "integration-test-generic-build"});
			var build = job.triggerBuild(this.get("repoBranches").toJSON());
			return build.success()
				.then(function () {
					scope._log("mergestart", "ITs passed succesfully, merging to master!");
					return scope.merge();
				}, function (error) {
					return scope._handleError(error);
				});
		},

		merge: function () {
			var job = new Job({name: "pull-request"});
			var params = _.extend({
				CHANGELOG: this.get("changelog"),
				CHANGELEVEL: "Level 1: (least impact) Functional defect resolution; some performance improvements"
			}, this.get("repoBranches").toJSON());

			return job.triggerBuild(params).success();
		},

		_log: function (type, message, data) {
			var params = _.extend({
				type: type,
				message: message
			}, data);
			this.get("logItems").put(new LogItem(params));
		},

		_handleError: function (error) {

		}
	});
});
