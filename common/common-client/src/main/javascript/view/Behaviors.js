define(function (require) {
	"use strict";
	var Marionette = require("marionette");

	var behaviors = {};

	Marionette.Behaviors.behaviorsLookup = function () {
		return behaviors;
	};

	return {
		registerBehavior: function (name, behavior) {
			behaviors[name] = behavior;
		}
	};
});