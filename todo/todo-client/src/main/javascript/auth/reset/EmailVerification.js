define(function (require) {
	"use strict";
	var _ = require("underscore");
    var Ajax = require("common/util/Ajax");
	var TboneModel = require("common/TboneModel");

    /**
     * @class EmailVerification
     */
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
			return Ajax.post({
				url: this.url(),
				data: {
					newPassword: newPassword
				}
			});
		}
	});
});