define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Marionette = require("marionette");
	var TboneView = require("common/TboneView");
	var Ladda = require("ladda");
	var Behaviors = require("common/view/Behaviors");

	Behaviors.registerBehavior("FormSubmit", Marionette.Behavior.extend({
		ui: {
			submit: "button[type=submit]"
		},

		events: {
			"submit": function () {
				var button = Ladda.create(this.ui.submit.get(0));
				button.start();
				this.view.onFormSubmit()
					.always(function () {
						button.stop();
					});

				return false;
			}
		}
	}));

	return TboneView.extend({
		tagName: "form",

		attributes: {
			role: "form"
		},

		behaviors: {
			FormSubmit: {}
		},

		constructor: function () {
			TboneView.prototype.constructor.apply(this, arguments);

			this.listenTo(this, "render", function () {
				var required = this.options.requiredProperties || _.keys(this.model.attributes);
				this.addBinding(null, "button[type=submit]", {
					observe: required,
					update: function (el, values) {
						var allPropsHaveValue = _.every(values, function (value) {
							return !!value;
						});
						el.prop("disabled", !allPropsHaveValue);
					}
				});
			});
		}
	});
});