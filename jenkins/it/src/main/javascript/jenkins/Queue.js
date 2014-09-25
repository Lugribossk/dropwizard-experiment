define(function (require) {
    "use strict";
    var $ = require("jquery");
    var _ = require("underscore");
    var Backbone = require("backbone");
    var Marionette = require("marionette");
    var TboneModel = require("common/TboneModel");
    var Promise = require("bluebird");
    var Logger = require("common/util/Logger");

    var log = new Logger("Queue");

    return TboneModel.extend({
        url: function () {
            return "/queue/item/" + this.get("id") + "/api/json";
        },

        success: function () {
            var scope = this;
            return this.fetch()
                .then(function () {
                    if (scope.get("executable")) {
                        log.info("Queue item executed.");
                        return scope;
                    } else if (scope.get("canceled")) {
                        log.info("Queue item canceled.");
                        return Promise.reject();
                    } else {
                        log.info("Queue item not resolved yet, refreshing in 10 seconds...");
                        return Promise.delay(null, 10000)
                            .then(function () {
                                return scope.success();
                            });
                    }
                });
        },

        getBuildNumber: function () {
            return this.get("executable").number;
        }
    }, {
        fromUrl: function (url) {
            var ThisClass = this;
            var id = /queue\/item\/(\d+)/.exec(url)[1];
            return new ThisClass({id: id});
        }
    });
});