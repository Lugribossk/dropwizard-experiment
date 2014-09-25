define(function (require) {
	"use strict";
	var $ = require("jquery");
	var _ = require("underscore");
	var Backbone = require("backbone");
	var Marionette = require("marionette");
	var TboneCompositeView = require("common/TboneCompositeView");
    var template = require("hbars!./MergeAttemptView");
    var LogItemView = require("jenkins/ui/LogItemView");

	return TboneCompositeView.extend({
        template: template,

        childView: LogItemView,
        childViewContainer: ".log",

        initialize: function () {
            this.collection = this.model.get("logItems");
        }
	});
});
