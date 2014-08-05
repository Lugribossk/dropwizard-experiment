define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Marionette = require("marionette");
    var TboneView = require("common/TboneView");
    var DeferredRegion = require("common/view/DeferredRegion");
    var Logger = require("common/util/Logger");
    var Promise = require("common/util/Promise");
    var template = require("hbars!./Modal");
    var confirmTemplate = require("hbars!./ModalConfirm");
    require("bootstrap");

    var log = new Logger("Modal");
    var modalRegion;
    var modalOpen = false;

    function initialize() {
        $("body").append("<div id='modal'></div>");
        modalRegion = new DeferredRegion({
            el: "#modal"
        });
    }

    function showModal(viewOptions) {
        if (modalOpen) {
            log.warn("Modal already open, ignoring request to open another");
            return Promise.rejected();
        }
        if (!modalRegion) {
            initialize();
        }

        var modalView = new ModalView(viewOptions);
        modalRegion.show(modalView);

        return modalView.okPromise;
    }

    var ModalView = TboneView.extend({
        getTemplate: function () {
            if (this.options.view) {
                return template;
            } else {
                return confirmTemplate;
            }
        },

        className: "modal",

        regions: {
            content: ".modal-body div"
        },

        ui: {
            cancelButton: ".btn.cancel",
            okButton: ".btn.ok"
        },

        events: {
            "click .ok": function () {
                // I guess this works because this event handler is called before the modal plugin notices that a data-dismiss element has been clicked.
                this.okPromise.resolve();
            }
        },

        okPromise: null,

        onRender: function () {
            var scope = this;
            this.okPromise = new $.Deferred();

            if (this.options.view) {
                $.when(this.options.view)
                    .done(function (view) {
                        scope.listenToOnce(view, "destroy", function () {
                            scope.$el.modal("hide");
                        });
                    });
                this.content.show(this.options.view);
            }

            if (!this.options.cancelLabel) {
                this.ui.cancelButton.hide();
            }

            this.$el.modal();
            this.$el.one("hidden.bs.modal", function () {
                scope.destroy();
            });
            modalOpen = true;
        },

        onAfterRender: function () {
            this.ui.okButton.focus();
        },

        onDestroy: function () {
            modalOpen = false;
            if (this.options.rejectOnDestroy) {
                this.okPromise.reject();
            } else {
                this.okPromise.resolve();
            }
        },

        templateHelpers: function () {
            return this.options;
        }
    });

    /**
     * Modal dialog controller. Can be used to show modals of various kinds.
     * Only one modal can be shown at a time, attempting to show another while one is open will have no effect.
     *
     * @class Modal
     */
    return Marionette.Controller.extend({

    }, {
        /**
         * Show a modal alert dialog.
         *
         * @static
         * @param {String} text
         * @param {String} okLabel
         * @returns {Promise} A promise for the modal having been closed.
         */
        alert: function (text, okLabel) {
            return showModal({
                text: text,
                cancelLabel: null,
                okLabel: okLabel || "Ok"
            });
        },

        /**
         * Show a modal confirm dialog.
         *
         * @static
         * @param {String} text
         * @param {String} okLabel
         * @returns {Promise} A promise for the modal having been closed by agreeing, rejects if closed in any other way.
         */
        confirm: function (text, okLabel) {
            return showModal({
                text: text,
                cancelLabel: "Cancel",
                okLabel: okLabel || "Ok",
                rejectOnDestroy: true
            });
        },

        /**
         * Show a view as the body of a modal.
         *
         * @static
         * @param {Backbone.View} view
         * @returns {Promise} A promise for the modal having been closed.
         */
        showView: function (view) {
            return showModal({view: view});
        }
    });
});