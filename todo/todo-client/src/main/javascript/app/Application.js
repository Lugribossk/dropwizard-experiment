import React from "react";
import Promise from "bluebird";
import "bootstrap/dist/css/bootstrap.css";
import ExampleNavbar from "./ExampleNavbar"
import ExampleRouteStore from "./ExampleRouteStore";
import LoginForm from "../auth/LoginForm"
import CurrentUserStore from "../auth/CurrentUserStore";
import Mixins from "../../../../../../common/common-client/src/main/javascript/util/Mixins";
import SubscribeMixin from "../../../../../../common/common-client/src/main/javascript/flux/SubscribeMixin";
import ExampleApi from "./ExampleApi";

export default class Application extends React.Component {
    constructor(props) {
        super(props);
        var api = new ExampleApi();
        this.currentUserStore = new CurrentUserStore(api);
        var routeStore = new ExampleRouteStore(window, this.currentUserStore);

        this.state = {
            user: this.currentUserStore.getUser(),
            route: routeStore.getRouteUrl(),
            routeContent: routeStore.getRouteContent()
        };

        this.subscribe(this.currentUserStore.onUserChange(this.onUserChange.bind(this)));
        this.subscribe(routeStore.onRouteChange(this.onRouteChange.bind(this)));
    }

    onUserChange(user) {
        this.setState({user: user});
    }

    onRouteChange(content, route) {
        this.setState({
            route: route,
            routeContent: content
        });
    }

    render () {
        if (this.state.user) {
            return (
                <div>
                    <ExampleNavbar {...this.state.user} route={this.state.route}/>
                    <div className="container">
                        {this.state.routeContent}
                    </div>
                </div>
            );
        } else {
            return (
                <div className="container">
                    <LoginForm userStore={this.currentUserStore}/>
                </div>
            );
        }
    }
}

Mixins.add(Application.prototype, [SubscribeMixin]);