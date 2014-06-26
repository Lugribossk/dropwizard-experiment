define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
	var Form = require("common/ui/Form");
	var template = require("hbars!./LoginForm");

    return Form.extend({
        template: template,

        ui: {
            warning: ".alert"
        },

        bindings: {
            "#username": "username",
            "#password": "password"
        },

        onFormSubmit: function () {
            var scope = this;
            this.ui.warning.hide();
            return this.controller.tryCredentials(this.model.get("username"), this.model.get("password"))
                .fail(function () {
                    scope.ui.warning.show();
                });
        },

        initialize: function () {
            this.model = new Backbone.Model({
                username: "",
                password: ""
            });
        }
    });
});