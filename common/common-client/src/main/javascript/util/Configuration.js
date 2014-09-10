define(function (require) {
    "use strict";
	var TboneModel = require("common/TboneModel");
	var ExampleApi = require("common/api/ExampleApi");

	var configurationPromise;

    /**
     * @class Configuration
     */
	return TboneModel.extend({
	}, {
		fetchCurrent: function () {
			if (!configurationPromise) {
				configurationPromise = this.fetch({
                    url: ExampleApi.getBaseUrl() + "/configurations/client"
                });
			}
			return configurationPromise;
		}
	});
});
