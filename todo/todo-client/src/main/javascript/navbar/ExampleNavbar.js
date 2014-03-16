define(function (require) {
    "use strict";
    var Navbar = require("common/ui/bootstrap/Navbar");
    var template = require("hbars!./ExampleNavbar");
    var Gravatar = require("common/ui/Gravatar");
    var AuthController = require("todo/auth/AuthController");
    require("less!./ExampleNavbar");

    return Navbar.extend({
        template: template,

        regions: {
            avatar: ".avatar"
        },

        bindings: {
            ".name": "name"
        },

        events: {
            "click .logout": function () {
                AuthController.logout();
                return false;
            }
        },

        onRender: function () {
            this.avatar.show(new Gravatar({model: this.model}));
        }
    });
});