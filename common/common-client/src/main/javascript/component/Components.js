define(function (require) {
    "use strict";
    var _ = require("underscore");
    var Marionette = require("marionette");

    /**
     * Component initialization manager. This takes care of initializing components on login and closing on logout.
     *
     * @cfg {User} currentUser
     * @cfg {ComponentNavBar} navbar
     */
    return Marionette.Controller.extend({
        initializedComponents: null,

        components: null,

        initialize: function () {
            this.initializedComponents = [];
            this.listenTo(this.options.currentUser, "change:isLoggedIn", function (model, isLoggedIn) {
                this._closeComponents();
                if (isLoggedIn) {
                    this._initializeComponents();
                }
            });
        },

        _initializeComponents: function () {
            var scope = this;
            _.each(this.components, function (component) {
                component.initialize(scope.options.currentUser)
                    .then(function () {
                        scope.initializedComponents.push(component);
                        if (component.getMenuItem) {
                            var item = component.getMenuItem();
                            if (item) {
                                scope.options.navbar.addComponentItem(item);
                            }
                        }
                    });
            });
        },

        _closeComponents: function () {
            _.each(this.initializedComponents, function (component) {
                // TODO What if a component has not finished initializing yet?
                if (component.close) {
                    component.close();
                }
            });
            this.initializedComponents = [];
            this.options.navbar.removeComponentItems();
        }
    });
});