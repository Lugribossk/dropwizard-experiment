define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneView = require("common/TboneView");
    var Modal = require("common/ui/bootstrap/Modal");
    var template = require("hbars!./Dashboard");

    return TboneView.extend({
        template: template,

        events: {
            "click .test1": function () {
                Modal.alert("test1");
            },
            "click .test2": function () {
                Modal.confirm("test2?");
            }
        }
    });
});