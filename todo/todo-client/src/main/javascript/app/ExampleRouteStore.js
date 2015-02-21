import React from "react";
import AuthenticatingRouter from "../../../../../../common/common-client/src/main/javascript/util/route/AuthenticatingRouter";

export default class ExampleRouteStore extends AuthenticatingRouter {
    constructor(window, userStore) {
        super(window, userStore);

        this.add("test1", () => {
            return (
                <h1>Test 1</h1>
            );
        });
        this.add("test2/:id", (id) => {
            return (
                <h1>Test 2 - {id}</h1>
            );
        });
        this.add("", () => {
            return (
                <h1>Dashboard</h1>
            );
        });

        this.init();
    }
}