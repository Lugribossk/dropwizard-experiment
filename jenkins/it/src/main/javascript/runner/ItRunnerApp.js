define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var RepoBranchesView = require("jenkins/ui/RepoBranchesView");
    var Logger = require("common/util/Logger");

	var app = new Marionette.Application();

	app.addRegions({
		content: "#main"
	});

    app.addInitializer(Logger.initialize);

	app.addInitializer(function () {
		this.content.show(new RepoBranchesView({region: this.content}));
	});

	return app;
});
