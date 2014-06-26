define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
	var Form = require("common/ui/Form");
	var template = require("hbars!./DoPasswordResetForm");

	function passwordsAreIdentical(values) {
		return values[0] === values[1];
	}

	return Form.extend({
		template: template,

		bindings: {
			"#password1": "password1",
			"#password2": "password2",
			".alert": {
				observe: ["password1", "password2"],
				visible: passwordsAreIdentical
			}
		},

		onFormSubmit: function () {
			return this.controller.changePassword(this.model.get("password1"));
		},

		allowSubmit: passwordsAreIdentical,

		initialize: function () {
			this.model = new Backbone.Model({
				password1: "",
				password2: ""
			});
		}
	});
});