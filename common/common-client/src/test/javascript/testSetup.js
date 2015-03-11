var jsdom = require("jsdom");

global.document = jsdom.jsdom("<!doctype html><html><body></body></html>");
global.window = document._global;
global.navigator = {
    userAgent: ""
};