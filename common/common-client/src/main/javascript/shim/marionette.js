define(function (require) {
    "use strict";
    var Marionette = require("real-marionette");
    var Promises = require("common/util/Promises");

    Promises.useWithMarionette(Marionette);

    return Marionette;
});