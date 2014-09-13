define(function (require) {
    "use strict";
    var _ = require("underscore");

    // Borrowed from https://github.com/timoxley/keycode/blob/master/index.js
    var codes = {
        "backspace": 8,
        "tab": 9,
        "enter": 13,
        "shift": 16,
        "ctrl": 17,
        "alt": 18,
        "pause/break": 19,
        "caps lock": 20,
        "esc": 27,
        "space": 32,
        "page up": 33,
        "page down": 34,
        "end": 35,
        "home": 36,
        "left": 37,
        "up": 38,
        "right": 39,
        "down": 40,
        "insert": 45,
        "delete": 46,
        "windows": 91,
        "right click": 93,
        "numpad *": 106,
        "numpad +": 107,
        "numpad -": 109,
        "numpad .": 110,
        "numpad /": 111,
        "num lock": 144,
        "scroll lock": 145,
        "my computer": 182,
        "my calculator": 183,
        ";": 186,
        "=": 187,
        ",": 188,
        "-": 189,
        ".": 190,
        "/": 191,
        "`": 192,
        "[": 219,
        "\\": 220,
        "]": 221,
        "'": 222
    };

    /**
     * Key code utilities.
     *
     * @class Keys
     */
    return {
        /**
         * Get the name of the specified keyboard event key code, if it exists.
         *
         * @param {Number} code
         * @returns {String}
         */
        nameForCode: function (code) {
            return _.find(_.keys(codes), function (name) {
                return codes[name] === code;
            });
        },
        codes: codes
    };
});