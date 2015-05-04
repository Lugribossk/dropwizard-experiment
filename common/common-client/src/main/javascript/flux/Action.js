import _ from "lodash";

var actions = {};

/**
 * A Flux "action" that components can trigger to change state in the stores.
 */
export default class Action {
    /**
     * @param {String} name A unique identifier for the action.
     * @returns {Function} Function that triggers the action (technically not instanceof Action).
     */
    constructor (name) {
        if (actions[name]) {
            return actions[name];
        }
        this.name = name;
        this.listeners = [];

        var me = this;
        function triggerAction(...args) {
            _.forEach(me.listeners, (listener) => {
                listener.apply(null, args);
            });
        }

        _.assign(triggerAction, this);
        // Trying to do this by mass-assigning from the prototype does not seem to work, I guess the methods are set as non-enumerable?
        triggerAction.onDispatch = this.onDispatch;

        actions[name] = triggerAction;
        return triggerAction;
    }

    /**
     * Listen for the action being triggered.
     * @param {Function} listener
     * @returns {Function} Function that removes the listener again
     */
    onDispatch(listener) {
        this.listeners.push(listener);
        return () => {
            _.remove(this.listeners, (el) => {
                return el === listener;
            });
        }
    }
}