define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Marionette = require("marionette");
    var TboneView = require("common/TboneView");
    var PromiseRegion = require("common/view/PromiseRegion");

    var toastEl;
    var toastRegion;

    function initialize() {
        toastEl = $("<div id='toast'></div>").appendTo("body");
        toastRegion = new PromiseRegion({
            el: toastEl
        });
    }

    function showToast(options) {
        if (!toastRegion) {
            initialize();
        }

        var toastView = new ToastView(options);
        toastRegion.show(toastView);
    }

    var ToastView = TboneView.extend({

    });

    /**
     * @class Toast
     */
    return Marionette.Controller.extend({

    }, {
        error: function (text) {
            showToast({
                text: text,
                type: "error"
            });
        },

        _destroy: function () {
            toastRegion.empty();
            toastRegion = null;
            toastEl.remove();
        }
    });
});