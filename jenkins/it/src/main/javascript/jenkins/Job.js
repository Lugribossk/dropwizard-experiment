define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var TboneModel = require("common/TboneModel");
	var Associations = require("associations");
	var Build = require("jenkins/jenkins/Build");

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

			return $.ajax({
				url: this.url(),
				type: "POST",
				data: {
					parameter: parameters
				}
			}).then(function () {
				// TODO get build number
				var number;
				return new Build({
					job: scope,
					number: number,
					triggerParameters: data
				});
			});
		}
	});
});
