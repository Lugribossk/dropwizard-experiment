define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Marionette = require("marionette");
	var TboneView = require("common/TboneView");
	var Ladda = require("ladda");
	var Behaviors = require("common/view/Behaviors");
    var Logger = require("common/util/Logger");

    var log = new Logger("Form");

	Behaviors.registerBehavior("FormSubmit", Marionette.Behavior.extend({
		ui: {
			submit: "button[type=submit]"
		},

		events: {
			"submit": function () {
                var scope = this;
				// setTimeout so we always return false and block the form submit.
                setTimeout(function () {
                    var button = Ladda.create(scope.ui.submit.get(0));
                    button.start();
                    var submitPromise = scope.view.onFormSubmit();

                    if (submitPromise) {
                        submitPromise.finally(function () {
                            if (!scope.isDestroyed) {
                                button.stop();
                            }
                        });
                    }
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