define(function (require) {
    "use strict";
    var Promise = require("tbone/util/Promise");
    var TboneView = require("tbone/TboneView");
    var template = require("hbars!./ProgressBar");

    /**
     * A progress bar that visually advances based on notifications from a promise.
     *
     * @cfg {Promise} [progress] The promise, should notify with percent complete.
     * @cfg {Boolean} [striped=true]
     * @cfg {Boolean} [active=true]
     * @cfg {String} [type=success]
     *
     * @class ProgressBar
     */
    return TboneView.extend({
        template: template,

        className: "progress progress-striped active",

        ui: {
            bar: ".progress-bar"
        },

        templateHelpers: function () {
            return {
                type: this.options.type || "success"
            };
        },

        onRender: function () {
            var scope = this;

            if (this.options.striped !== false) {
                this.$el.addClass("progress-striped");
            }

            if (this.options.active !== false) {
                this.$el.addClass("active");
            }

            if (this.options.progress) {
                this.options.progress
                    .progress(function (percent) {
                        if (!scope.isClosed) {
                            scope.setProgress(percent);
                        }
                    });
            }
        },

        setProgress: function (percent) {
            this.ui.bar.css({width: (percent * 100) + "%"});
        }
    });
});