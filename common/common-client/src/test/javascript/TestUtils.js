/*global beforeEach, afterEach*/
define(function (require) {
	"use strict";
	var $ = require("jquery");
	var Marionette = require("marionette");

	return {
		createTestDom: function () {
			var test = {};
			beforeEach(function () {
				var el = $("<div></div>").appendTo("body");
				var region = new Marionette.Region({el: el});

				test.el = el;
				test.region = region;
				test.find = el.find.bind(el);
				test.show = region.show.bind(region);
                test.input = function (selector, text) {
                    this.find(selector).val(text).trigger("change");
                };
			});
			afterEach(function () {
				test.region.empty();
				test.el.remove();
				delete test.show;
				delete test.find;
                delete test.input;
			});
			return test;
		}
	};
});