define(function (require) {
    "use strict";
    var Backbone = require("real-backbone");
    var Promises = require("common/util/Promises");

    Promises.useWithBackbone(Backbone);

    return Backbone;
});