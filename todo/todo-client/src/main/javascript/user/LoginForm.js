define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneView = require("tbone/TboneView");
    var template = require("hbars!./LoginForm");
    var Ladda = require("ladda");

    return TboneView.extend({
        template: template,

        tagName: "form",

        attributes: {
            role: "form"
        },

        ui: {
            submit: "button[type=submit]",
            warning: ".alert"
        },

        bindings: {
            "#username": "username",
            "#password": "password",
            "button[type=submit]": {
                observe: ["username", "password"],
                update: function (el, values) {
                    el.prop("disabled", !(values[0] && values[1]));
                }
            }
        },

        events: {
            "submit": function () {
                var scope = this;
                scope.ui.warning.hide();
                // TODO why does this work here but not in onRender?
                var button = Ladda.create(this.ui.submit.get(0));
                button.start();

                this.controller.tryCredentials(this.model.get("username"), this.model.get("password"))
                    .fail(function () {
                        scope.ui.warning.show();
                    })
                    .always(function () {
                        button.stop();
                    });

                return false;
            }
        },

        initialize: function () {
            this.model = new Backbone.Model({
                username: "",
                password: ""
            });
        }
    });
});