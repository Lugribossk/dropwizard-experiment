import React from "react";
import _ from "lodash";

/**
 * A Flux "store", a repository of data state that listens to actions and triggers events when changed.
 */
export default class Store {
    constructor() {
        this.listeners = {};
        this.state = {};
    }

    /**
     * Change the stored data.
     * @param {Object} newState
     */
    setState(newState) {
        _.assign(this.state, newState);
        _.forEach(newState, (value, key) => {
            this._trigger(key, value);
        });
    }

    /**
     * Register a listener that automatically triggers when the named key in state is changed.
     * @param {String} name
     * @param {Function} listener
     * @returns {Function}
     * @private
     */
    _registerListener(name, listener) {
        if (!this.listeners[name]) {
            this.listeners[name] = [];
        }
        this.listeners[name].push(listener);

        return () => {
            _.remove(this.listeners, (el) => {
                return el === listener;
            });
        }
    }

    /**
     * Trigger listeners with the specified name with the provided data.
     * @param {String} name
     * @param {*} data
     * @private
     */
    _trigger(name, ...data) {
        if (this.listeners[name]) {
            _.forEach(this.listeners[name], (listener) => {
                listener.apply(null, data);
            });
        }
    }
}