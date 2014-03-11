define(function (require) {
	"use strict";
	var _ = require("underscore");
	var Backbone = require("backbone");

	/**
	 * A "view" of another collection that can be sorted and filtered, without modifying the original collection.
	 *
	 * @class ProxyCollection
	 */
	return Backbone.Collection.extend({
		constructor: function (realCollection, options) {
			Backbone.Collection.prototype.constructor.call(this, realCollection.model, options);

			this._realCollection = realCollection;
			this.listenTo(realCollection, "add", this.add);
			this.listenTo(realCollection, "remove", this.remove);
		},

		filter: function (select) {
			this.reset(this._realCollection.filter(select));
			this.sort();
		},

		sort: function (comparator) {
			if (comparator) {
				this.comparator = comparator;
			}
			Backbone.Collection.prototype.sort.call(this);
		}
	});
});