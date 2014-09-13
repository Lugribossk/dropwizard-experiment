define(function (require) {
    "use strict";
	var TboneModel = require("common/TboneModel");

	var configurationPromise;

    /**
     * @class Configuration
     */
	return TboneModel.extend({
        urlRoot: "/configurations/client"
	}, {
		fetchCurrent: function () {
			if (!configurationPromise) {
				configurationPromise = this.fetch();
			}
			return configurationPromise;
		}
	});
});
