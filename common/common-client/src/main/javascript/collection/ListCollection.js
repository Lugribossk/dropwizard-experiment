define(function (require) {
    "use strict";
    var _ = require("underscore");
    var Backbone = require("backbone");

    var ListItemModel = Backbone.Model.extend({
        constructor: function (item, options) {
            Backbone.Model.prototype.constructor.call(this, {
                value: item
            }, options);
        }
    });

    /**
     * Adapter for using primitive lists as Backbone.Collections, e.g. in a CollectionView.
     *
     * @class ListCollection
     */
    return Backbone.Collection.extend({
        constructor: function (list, options) {
            Backbone.Collection.prototype.constructor.call(this, _.map(list, function (item) {
                return new ListItemModel(item, options);
            }), options);
        }
    });
});