define(function (require) {
    "use strict";
    var _ = require("underscore");
    var Ajax = require("common/util/Ajax");
    var TboneController = require("common/TboneController");
	var RequestPasswordResetForm = require("todo/auth/reset/RequestPasswordResetForm");
	var DoPasswordResetForm = require("todo/auth/reset/DoPasswordResetForm");
	var EmailVerification = require("todo/auth/reset/EmailVerification");
    var Promise = require("bluebird");
	var Logger = require("common/util/Logger");

	var log = new Logger("PasswordResetController");

    return TboneController.extend({
		requestPasswordReset: function (username) {
			return Ajax.post({
				url: this.url() + "/verifications/passwordreset",
				data: {
					username: username
				}
			});
		},

        changePassword: function (newPassword) {
            return this.model.changePassword(newPassword)
                .then(function () {
                    //DashboardController.show();
                });
        }
    }, {
        showRequestResetPasswordForm: function (region) {
			this._showView(region, null, RequestPasswordResetForm);
        },

        showDoPasswordResetForm: function (region, id) {
			this._showView(region, EmailVerification.fetchById(id)
				.then(function (verification) {
					if (!verification.isPasswordReset()) {
						log.error("Got email verification that was not for password reset:", verification);
						return Promise.reject();
					}
				}), DoPasswordResetForm);
        }
    });
});