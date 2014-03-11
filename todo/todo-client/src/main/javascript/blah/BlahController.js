define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");

    return TboneController.extend({
        viewType: BlahView

        /*showBlah: function (id) {
            this._showModel(Blah.fetchById(id));


            var scope = this;
            this.options.region.show(Blah.fetchById(id)
                .then(function (blah) {
                    scope.model = blah;
                    return new BlahView({
                        controller: scope,
                        model: blah
                    });
                }));
        }*/
    }, {
        showBlah: function (id, region) {
            this._showModel(Blah.fetchById(id), region);
        }
    });
});