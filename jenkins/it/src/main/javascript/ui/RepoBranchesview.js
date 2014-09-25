define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var Form = require("common/ui/Form");
	var TboneModel = require("common/TboneModel");
	var MergeAttempt = require("jenkins/runner/MergeAttempt");
	var MergeAttemptView = require("jenkins/ui/MergeAttemptView");
	var template = require("hbars!./RepoBranchesView");

	return Form.extend({
		template: template,

		bindings: {
			".integration-test": "INTEGRATION_TEST_GIT_REF",
			".apps": "APPS_GIT_REF"
		},

		initialize: function () {
			this.model = new TboneModel();
			this.changelog = new TboneModel();
		},

		onRender: function () {
			this.stickit(this.changelog, {
				".changelog": "message"
			});

			/*var blah = UrlParameters.fromHash();
			_.each(blah, function (value, name) {
				scope.set(name, value);
			});*/
		},

		onFormSubmit: function () {
			var attempt = new MergeAttempt({
				repoBranches: this.model.toJSON(),
				changelog: this.changelog.get("message")
			});
			attempt.startITs();
			return this.options.region.show(new MergeAttemptView({model: attempt}));
		}
	});
});
