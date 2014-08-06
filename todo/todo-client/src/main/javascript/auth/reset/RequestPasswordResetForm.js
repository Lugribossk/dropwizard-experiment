define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
	var Form = require("common/ui/Form");
	var template = require("hbars!./RequestPasswordResetForm");

	return Form.extend({
		template: template,

		ui: {
			info: ".alert"
		},

		bindings: {
			"#username": "username"
		},

		onFormSubmit: function () {
			var scope = this;
			this.controller.requestPasswordReset()
				.then(function () {
					scope.ui.info.show();
				});
		},

		initialize: function () {
			this.model = new Backbone.Model({
				username: ""
			});
		}
	});
});