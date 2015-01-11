define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneModel = require("common/TboneModel");
    var BranchCollection = require("dashboard/github/BranchCollection");

    return TboneModel.extend({

        getBranches: function () {
            return BranchCollection.fetchByRepo(this);
        }
    }, {
        fetchByName: function (name, owner) {
            return this.fetch({
                url: "https://api.github.com/repos/" + owner + "/" + name
            });
        }
    });
});