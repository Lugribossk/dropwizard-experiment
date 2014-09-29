define(function (require) {
    "use strict";
    var ProgressBar = require("common/views/ProgressBar");
    var Logger = require("common/util/Logger");

    var log = new Logger("FakeProgressBar");
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
            var duration = timings[this.options.key] || this.options.defaultDuration || 2000;
            var maxSteps = 20;
            var currentSteps = 0;

            var handle = setInterval(function () {
                if (currentSteps === maxSteps || scope.isDestroyed) {
                    clearInterval(handle);
                }

                // Only progress towards 90% complete so the bar looks slightly better if it takes longer.
                scope.setProgress(currentSteps / maxSteps * 0.9);
                currentSteps++;
            }, duration / maxSteps);
        },

        onDestroy: function () {
            var realDuration = Date.now() - this.startTime;
            timings[this.options.key] = realDuration;
            log.info("Duration for", this.options.key, "was", realDuration);
        }
    });
});