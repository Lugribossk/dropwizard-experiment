import Store from "./Store";

export default class CachingStore extends Store {
    constructor(storageKey) {
        super();
        this.storageKey = storageKey;
        window.addEventListener("unload", this.saveToLocalStorage.bind(this));
    }

    saveToLocalStorage() {
        localStorage.setItem(this.storageKey, JSON.stringify(this.state));
    }

    getCachedState() {
        var rawData = localStorage.getItem(this.storageKey);
        if (rawData) {
            var data = JSON.parse(rawData);
            if (this.unmarshalState) {
                data = this.unmarshalState(data);
            }
            return data;
        } else {
            return null;
        }
    }
}