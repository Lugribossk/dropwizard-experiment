import React from "react";
import md5 from "md5";

export default class Gravatar extends React.Component {
    getUrl() {
        var hash = md5.digest_s(this.props.email.toLocaleLowerCase());
        var size = Math.ceil(this.props.size * (window.devicePixelRatio || 1));
        return "https://secure.gravatar.com/avatar/" + hash + "?d=mm&s=" + size;
    }

    render() {
        return (
            <img src={this.getUrl()}/>
        );
    }
}

Gravatar.propTypes = {
    email: React.PropTypes.string.isRequired
};