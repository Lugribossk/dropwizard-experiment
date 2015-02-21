import _ from "lodash";

var actions = {};

export default class Action {
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
        // Trying to do this by mass-assigning from the prototype does not seem to work.
        triggerAction.onDispatch = this.onDispatch;

        actions[name] = triggerAction;
        return triggerAction;
    }

    onDispatch(listener) {
        this.listeners.push(listener);
        return () => {
            _.remove(this.listeners, (el) => {
                return el === listener;
            });
        }
    }
}