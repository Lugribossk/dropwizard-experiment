define(function (require) {
    "use strict";
    var Components = require("tbone/component/Components");

    return Components.extend({
        components: [require("blah/shared/BlahComponent")]
    });
});