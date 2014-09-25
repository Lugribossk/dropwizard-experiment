define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var RepoBranchesView = require("jenkins/ui/RepoBranchesView");

	var app = new Marionette.Application();

	app.addRegions({
		content: "#main"
	});

	app.addInitializer(function () {
		this.content.show(new RepoBranchesView({region: this.content}));
	});

	return app;
});
