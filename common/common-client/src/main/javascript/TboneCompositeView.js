define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var ViewFeatures = require("common/view/ViewFeatures");

    /**
     * @class TboneCompositeView
     * @extends Marionette.CompositeView
     */
    return Marionette.CompositeView.extend({
        constructor: function (options) {
            Marionette.CompositeView.prototype.constructor.call(this, options);

            ViewFeatures.all(this);
        }
    });
});