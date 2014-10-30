define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var TboneModel = require("common/TboneModel");

	return TboneModel.extend({
		urlRoot: "api/verifications",

		idAttribute: "token",

		defaults: {
			token: null,
			expirationDate: null,
			type: null
		},

		isPasswordReset: function () {
			return this.get("type") === "P";
		},

		changePassword: function (newPassword) {
			return $.ajax({
				url: this.url(),
				type: "POST",
				data: {
					newPassword: newPassword
				}
			});
		}
	});
});