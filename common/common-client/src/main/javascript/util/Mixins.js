import _ from "lodash";

var lifecycleMethods = [
    "componentWillMount",
    "componentDidMount",
    "componentWillReceiveProps",
    "componentWillUpdate",
    "componentDidUpdate",
    "componentWillUnmount"
];

export default {
    add: function (context, mixins) {
        // Several mixins (and the class being mixed into) can use the React lifecycle notification methods.
        // So collect all of each type together and create a single function that calls all of them.
        _.forEach(lifecycleMethods, (methodName) => {
            var mixedInMethods = [];
            _.forEach(mixins, (mixin) => {
                if (mixin[methodName]) {
                    mixedInMethods.push(mixin[methodName]);
                }
            });

            if (mixedInMethods.length > 0) {
                if (context[methodName]) {
                    mixedInMethods.push(context[methodName]);
                }
                context[methodName] = function (...args) {
                    _.forEach(mixedInMethods, (method) => {
                        method.apply(this, args);
                    });
                }
            }
        });

        // Directly assign any remaining methods.
        // (There's no way to resolve duplicates in this case as we do not know the intention or return value of them.)
        _.forEach(mixins, (mixin) => {
            _.forEach(mixin, (method, methodName) => {
                if (!_.contains(lifecycleMethods, methodName)) {
                    if (context[methodName]) {
                        throw new Error("Context being mixed into already has method named " + methodName);
                    }
                    context[methodName] = method;
                }
            })
        });
    }
}