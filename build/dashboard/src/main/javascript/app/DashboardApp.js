define(function (require) {
    "use strict";
    var Marionette = require("marionette");
    var Logger = require("common/util/Logger");
    var PromiseRegion = require("common/view/PromiseRegion");
    var Promise = require("bluebird");
    var CircleCiApi = require("dashboard/circleci/CircleCiApi");
    var GithubApi = require("dashboard/github/GithubApi");
    var BranchesView = require("dashboard/branches/BranchesView");

    var app = new Marionette.Application();

    app.addRegions({
        content: {
            selector: "#main",
            regionClass: PromiseRegion
        }
    });

    app.addInitializer(Logger.initialize);

    app.addInitializer(function () {
        var circleCi = new CircleCiApi();
        var github = new GithubApi();

        this.content.show(Promise.all(circleCi.getRecentBuilds(""), github.getRepository("").getBranches())
            .then(function (builds, branches) {

                return new BranchesView({
                    collection: branches,
                    builds: builds
                });
            }));
    });

    return app;
});