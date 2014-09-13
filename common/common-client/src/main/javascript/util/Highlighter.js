define(function (require) {
    "use strict";
    var Handlebars = require("Handlebars");

    function escape(text) {
        return Handlebars.Utils.escapeExpression(text);
    }

    /**
     * Utility class for highlighting occurrences of a query inside a longer string.
     *
     * @param {String} start The highlight marker before an occurrence of the query.
     * @param {String} end The highlight marker after an occurrence of the query.
     * @constructor
     */
    function Highlighter(start, end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Highlight occurrences of the query in the specified text.
     *
     * @param {String} text
     * @param {String} query
     * @returns {String}
     */
    Highlighter.prototype.highlight = function (text, query) {
        if (!query || query === "") {
            return text;
        }
        var index = 0;
        var output = "";

        while (index < text.length) {
            var nextQueryStart = text.toLocaleLowerCase().indexOf(query, index);
            if (nextQueryStart === -1) {
                output += escape(text.substr(index, text.length));
                break;
            } else {
                output += escape(text.substr(index, nextQueryStart - index));
                output += this.start;
                output += escape(text.substr(nextQueryStart, query.length));
                output += this.end;
                index = nextQueryStart + query.length;
            }
        }

        return output;
    };

    /**
     * Create a Stickit binding configuration that highlights the observed text.
     *
     * @static
     * @param {String} name The model property to observe.
     * @param {String} query The query to highlight.
     * @returns {Object}
     */
    Highlighter.binding = function (name, query) {
        return {
            observe: name,
            updateMethod: "html",
            onGet: function (value) {
                return new Highlighter("<span class='highlight'>", "</span>").highlight(value, query);
            }
        };
    };

    return Highlighter;
});