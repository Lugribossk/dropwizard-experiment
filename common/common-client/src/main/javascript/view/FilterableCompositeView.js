define(function (require) {
    "use strict";
    var Backbone = require("backbone");
    var TboneCompositeView = require("common/TboneCompositeView");

    /**
     * CompositeView that can be filtered to only show some of the elements in its collection.
     *
     * This is accomplished by only creating child views for the visible elements, rather than modifying the collection itself.
     *
     * @class FilterableCompositeView
     */
    return TboneCompositeView.extend({
        constructor: function (options) {
            this.model = new Backbone.Model({query: ""});
            TboneCompositeView.prototype.constructor.call(this, options);
        },

        ui: {
            searchQuery: "input"
        },

        bindings: {
            input: "query"
        },

        modelEvents: {
            "change:query": "_renderChildren"
        },

        onAfterRender: function () {
            this.ui.searchQuery.focus();
        },

        filter: function (item, query) {
            return true;
        },

        addChild: function (model, ViewClass, index) {
            if (this.filter(model, this.model.get("query"))) {
                return TboneCompositeView.prototype.addChild.call(this, model, ViewClass, index);
            }
        }
    });
});