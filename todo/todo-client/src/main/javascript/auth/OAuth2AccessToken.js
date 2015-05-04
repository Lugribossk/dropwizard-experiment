export default class OAuth2AccessToken {
    constructor(data) {
        this.accessToken = data.accessToken;
        this.expirationDate = data.expirationDate;
    }
}