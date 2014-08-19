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
				var scope = this;
				// setTimeout so an error in the code won't stop us from returning false and blocking the submit.
				setTimeout(function () {
					var button = Ladda.create(scope.ui.submit.get(0));
					button.start();
					scope.view.onFormSubmit()
						.always(function () {
							button.stop();
						});

				}, 0);

				return false;
			}
		}
	}));

	/**
	 * A form with an intelligent submit button.
	 *
	 * @cfg {Function} onFormSubmit
	 * @cfg {Function} [allowSubmit]
	 * @cfg {String[]} [requiredProperties]
	 *
	 * @class Form
	 */
	return TboneView.extend({
		tagName: "form",

		attributes: {
			role: "form"
		},

		behaviors: {
			FormSubmit: {}
		},

		constructor: function () {
			TboneView.prototype.constructor.apply(this, _.toArray(arguments));
			var scope = this;

			this.listenTo(this, "render", function () {
				var required = this.requiredProperties || _.keys(this.model.attributes);
				this.addBinding(null, "button[type=submit]", {
					observe: required,
					update: function (el, values) {
						var allPropsHaveValue = _.every(values, function (value) {
							return !!value;
						});
						var extraValidation = true;
						if (scope.allowSubmit && !scope.allowSubmit(values)) {
							extraValidation = false;
						}
						el.prop("disabled", !(allPropsHaveValue && extraValidation));
					}
				});
			});
		}
	});
});