define(function (require) {
    "use strict";
    var Components = require("common/component/Components");

    return Components.extend({
        components: [require("blah/shared/BlahComponent")]
    });
});