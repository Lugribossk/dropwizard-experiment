define(function (require) {
	"use strict";
	var TboneModel = require("common/TboneModel");

	/**
	 * A Jenkins user account.
	 *
	 * @class User
	 */
	return TboneModel.extend({
		defaults: {
			fullName: null
		},

		url: function () {
			return "/user/" + this.get("id") + "/api/json";
		}
	}, {
		/**
		 * Fetch the currently logged in user.
		 * @returns {User}
		 */
		fetchCurrent: function () {
			return this.fetch({
				url: "/me/api/json"
			});
		}
	});
});
