define(function (require) {
    "use strict";
    var _ = require("underscore");
    var Keys = require("common/util/Keys");
    require("stickit");

    /**
     * @class ViewFeatures
     */
    return {
        all: function (view) {
            // Automatically enable Stickit.
            if (view.bindings) {
                view.listenTo(view, "render", function () {
                    view.stickit();
                });
            }

            view.listenTo(view, "render", function () {
                setTimeout(function () {
                    if (!view.isDestroyed) {
                        view.triggerMethod("after:render");
                    }
                }, 0);
            });

            // Set up keyboard event handling.
            if (view.keyboardEvents) {
                view.listenTo(view, "render", function () {
                    var onKeyDown = function (e) {
                        var keyName = Keys.nameForCode(e.keyCode);
                        var handler = view.keyboardEvents[keyName];
                        if (_.isString(handler)) {
                            handler = view[handler];
                        }
                        if (handler) {
                            e.preventDefault();
                            handler.call(view, e);
                        }
                    };

                    view.$el.on("keydown", onKeyDown);
                    view.listenToOnce(view, "destroy", function () {
                        view.$el.off("keydown", onKeyDown);
                    });
                });
            }
        }
    };
});