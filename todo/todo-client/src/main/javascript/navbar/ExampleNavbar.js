define(function (require) {
    "use strict";
    var Navbar = require("tbone/ui/bootstrap/Navbar");
    var template = require("hbars!./ExampleNavbar");
    var Gravatar = require("tbone/ui/Gravatar");
    var LoginController = require("user/LoginController");
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
                LoginController.logout();
                return false;
            }
        },

        onRender: function () {
            this.avatar.show(new Gravatar({model: this.model}));
        }
    });
});