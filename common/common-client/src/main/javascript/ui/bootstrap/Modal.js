define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneView = require("tbone/TboneView");
    var Bootstrap = require("Bootstrap");
    var template = require("hbars!./Modal");

    var modalRegion;

    function initialize() {
        $("body").append("<div id='modal'></div>");
        modalRegion = new Marionette.Region({
            el: "#modal"
        });
    }

    var ModalView = TboneView.extend({
        template: template,

        className: "modal",

        regions: {
            content: ".modal-body div"
        },

        onRender: function () {
            var scope = this;
            $.when(this.options.view)
                .done(function (view) {
                    scope.listenToOnce(view, "close", function () {
                        modalRegion.close();
                    });
                });
            this.content.show(this.options.view);
//            this.$el.find(".modal-header")
//                .prepend("<button type='button' class='close' data-dismiss='modal'>&times;</button>");

            this.$el.modal();
        }
    });

    /**
     * @class Modal
     */
    return {
        show: function (view) {
            if (!modalRegion) {
                initialize();
            }
            modalRegion.show(new ModalView({view: view}));
        }
    };
});