import React from "react";
import {Navbar, Nav, NavItem, DropdownButton, MenuItem} from "react-bootstrap"
import Gravatar from "../../../../../../common/common-client/src/main/javascript/ui/Gravatar";
import AuthActions from "../auth/AuthActions";

export default class ExampleNavbar extends React.Component {
    isActive(link) {
        var route = this.props.route;
        var isDefault = (route === "" && link === "#");
        var isSubpath = (route.indexOf(link) === 0);

        return isDefault || isSubpath
    }

    render () {
        var currentUser = (
            <span>
                <Gravatar email={this.props.email} size={30}/>
                <span> {this.props.name}</span>
            </span>
        );

        return (
            <Navbar brand="Example">
                <Nav>
                    <NavItem href="#" active={this.isActive("#")}>Dashboard</NavItem>
                    <NavItem href="#test1" active={this.isActive("test1")}>Test 1</NavItem>
                    <NavItem href="#test2/12345" active={this.isActive("test2")}>Test 2</NavItem>
                </Nav>
                <Nav right={true}>
                    <DropdownButton title={currentUser} className="name-dropdown">
                        <MenuItem href="#profile">Profile</MenuItem>
                        <MenuItem divider />
                        <MenuItem onSelect={AuthActions.logout} className="logout">Log out</MenuItem>
                    </DropdownButton>
                </Nav>
            </Navbar>
        );
    }
}

ExampleNavbar.propTypes = {
    email: React.PropTypes.string.isRequired,
    name: React.PropTypes.string.isRequired,
    route: React.PropTypes.string.isRequired
};