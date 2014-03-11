define(function (require) {
    "use strict";
    var ProgressBar = require("tbone/views/ProgressBar");
    var Promise = require("tbone/util/Promise");

    var timings = {};

    /**
     * A progress bar that visually advances based on a guess for how long it will take in total.
     *
     * @cfg {String} key A value that should be identical for progress that waits for similar events.
     * @cfg {Number} [defaultDuration=2000]
     *
     * @class FakeProgressBar
     */
    return ProgressBar.extend({
        onRender: function () {
            var scope = this;
            this.startTime = Date.now();

            // TODO use css animation instead
            var duration = timings[this.options.key] || this.options.defaultDuration || 2000;
            Promise.delay(duration, 20)
                .progress(function (percent) {
                    if (!scope.isClosed) {
                        // Only progress towards 90% complete so the bar looks slightly better if it takes longer.
                        scope.setProgress(percent * 0.9);
                    }
                });
        },

        onClose: function () {
            timings[this.options.key] = Date.now() - this.startTime;
        }
    });
});