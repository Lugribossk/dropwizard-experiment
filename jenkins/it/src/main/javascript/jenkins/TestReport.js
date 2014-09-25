define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var TboneModel = require("common/TboneModel");

	return TboneModel.extend({
		defaults: {
			failCount: null,
			passCount: null,
			build: null
		},

		url: function () {
			return "/job/" + this.get("build.job.name") + "/" + this.get("build.number") + "/testReport/api/json";
		},

		getFailedCases: function () {
			var failedCases = [];
			_.each(this.get("suites"), function (suite) {
				_.each(suite.cases, function (kase) {
					if (kase.status === "FAILED") {
						failedCases.push(kase);
					}
				});
			});
			return failedCases;
		}
	});
});
