define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var TboneModel = require("common/TboneModel");

	return TboneModel.extend({
		defaults: {
			fullName: null
		},

		url: function () {
			return "/user/" + this.get("id") + "/api/json";
		}
	}, {
		fetchCurrent: function () {
			return this.fetch({
				url: "/me/api/json"
			});
		}
	});
});
