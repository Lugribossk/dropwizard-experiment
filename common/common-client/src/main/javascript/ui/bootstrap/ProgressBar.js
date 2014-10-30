define(function (require) {
    "use strict";
    var TboneView = require("common/TboneView");
    var template = require("hbars!./ProgressBar");

    /**
     * A progress bar that visually advances.
     *
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

        onRender: function () {
            if (this.options.striped !== false) {
                this.$el.addClass("progress-striped");
            }

            if (this.options.active !== false) {
                this.$el.addClass("active");
            }

            this.ui.bar.addClass("progress-bar-" + this.options.type || "success");
        },

        setProgress: function (percent) {
            this.ui.bar.css({width: (percent * 100) + "%"});
        }
    });
});