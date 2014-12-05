define(function (require) {
    "use strict";
    var Backbone = require("real-backbone");
    var Ajax = require("common/util/Ajax");

    Ajax.useWithBackbone(Backbone);

    return Backbone;
});