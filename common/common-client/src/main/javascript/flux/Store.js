import React from "react";

export default class Store {
    constructor() {
        this.listeners = {};
        this.state = {};
    }

    setState(newState) {
        _.assign(this.state, newState);
        _.forEach(newState, (value, key) => {
            this._trigger(key, value);
        });
    }

    _registerListener(name, listener) {
        /*if (listener instanceof React.Component) {
            listener.state[name] = this.state[name];
            var newListener = () => {
                listener.setState({[name]: this.state[name]});
            };
            return this._registerListener(name, newListener);
        }*/

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

    _trigger(name, ...data) {
        if (this.listeners[name]) {
            _.forEach(this.listeners[name], (listener) => {
                listener.apply(null, data);
            });
        }
    }
}