import _ from "lodash";
import request from "superagent";
import Promise from "bluebird";

export default class OAuth2Api {
    constructor() {
        this.token = null;
    }

    authenticateWith(token) {
        this.token = token;
    }

    get(url, data) {
        return new Promise((resolve, reject) => {
            var req = request.get(this.getBaseUrl() + url);

            if (this.token) {
                this.authenticateRequest(req);
            }
            if (data) {
                req.query(data);
            }

            req.end((err, result) => {
                if (err || result.error) {
                    reject(err || result.error);
                } else {
                    resolve(result.body);
                }
            });
        });
    }

    getAs(url, data, Klass) {
        if (data && !_.isPlainObject(data)) {
            Klass = data;
        }
        return this.get(url, data)
            .then((data) => {
                return new Klass(data);
            });
    }

    getAsList(url, data, Klass) {
        if (data && !_.isPlainObject(data)) {
            Klass = data;
        }
        return this.get(url, data)
            .then((items) => {
                return _.map(items, (data) => {
                    return new Klass(data);
                });
            });
    }

    post(url, data) {
        return new Promise((resolve, reject) => {
            request
                .post(this.getBaseUrl() + url)
                .send(data)
                .set("Content-Type", "application/x-www-form-urlencoded")
                .end((err, result) => {
                    if (err || result.error) {
                        reject(err || result.error);
                    } else {
                        resolve(result.body);
                    }
                });
        });
    }

    getBaseUrl() {
        return "";
    }

    authenticateRequest(req) {
        req.set("Authorization", "Bearer " + this.token);
    }
}