import _ from "lodash";

/**
 * Component mixin for automatically unregistering listeners on unmount.
 */
export default {
    subscribe: function (unsubscriber) {
        if (!this.unsubscribers) {
            this.unsubscribers = [];
        }
        this.unsubscribers.push(unsubscriber);
    },

    componentWillUnmount: function () {
        _.forEach(this.unsubscribers, (unsubscriber) => {
            unsubscriber();
        })
    }
}