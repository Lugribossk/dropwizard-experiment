/*global module, require*/
var webpack = require("webpack");
var HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = function (grunt) {
    "use strict";

    /**
     * Development utility tasks.
     */

    grunt.config.set("webpack-dev-server", {
        options: {
            webpack: {
                context: "src/main/javascript",
                entry: [
                    "webpack-dev-server/client?http://localhost:8080",
                    "webpack/hot/only-dev-server",
                    "./main.js"
                ],
                output: {
                    path: "target",
                    filename: "main.js"
                },
                module: {
                    loaders: [
                        { test: /\.js$/, exclude: /node_modules/, loaders: ["react-hot", "babel?sourceMap=true"]},
                        { test: /\.css$/, loader: "style!css"},
                        { test: /\.woff2?(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&minetype=application/font-woff" },
                        { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&minetype=application/octet-stream" },
                        { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file" },
                        { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&minetype=image/svg+xml" }
                    ]
                },
                plugins: [
                    new HtmlWebpackPlugin({
                        template: "src/main/javascript/index.html"
                    }),
                    new webpack.HotModuleReplacementPlugin(),
                    new webpack.NoErrorsPlugin()
                ],
                node: {
                    __filename: true
                },
                watch: true,
                keepalive: true
            },
            publicPath: "/",
            hot: true
        },
        start: {
            keepAlive: true,
            webpack: {
                devtool: "eval",
                debug: true
            }
        }
    });

    grunt.registerTask("dev", ["webpack-dev-server:start"]);
};