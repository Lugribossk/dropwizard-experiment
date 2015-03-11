/*global module, require*/
var webpack = require("webpack");
var HtmlWebpackPlugin = require("html-webpack-plugin");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = function (grunt) {
    "use strict";

    /**
     * Tasks for producing the final build output.
     */

    require("grunt-webpack/tasks/webpack")(grunt);
    grunt.config.set("webpack", {
        build: {
            context: "src/main/javascript",
            entry: {
                main: "./main.js",
                vendor: ["bluebird", "lodash", "md5", "react", "react-bootstrap", "superagent"]
            },
            output: {
                path: "target/dist",
                filename: "main-[chunkhash].min.js"
            },
            module: {
                loaders: [
                    { test: /\.js$/, exclude: /node_modules/, loader: "babel"},
                    { test: /\.css$/, loader: ExtractTextPlugin.extract("style", "css")},
                    { test: /\.woff2?(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&minetype=application/font-woff" },
                    { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&minetype=application/octet-stream" },
                    { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file" },
                    { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&minetype=image/svg+xml" }
                ]
            },
            plugins: [
                new webpack.optimize.OccurenceOrderPlugin(),
                new webpack.optimize.CommonsChunkPlugin("vendor", "vendor-[chunkhash].min.js"),
                new HtmlWebpackPlugin({
                    template: "src/main/javascript/index-build.html"
                }),
                new webpack.DefinePlugin({
                    "process.env": {
                        NODE_ENV: JSON.stringify("production")
                    }
                }),
                new ExtractTextPlugin("main-[chunkhash].css"),
                new webpack.optimize.UglifyJsPlugin({
                    minimize: true,
                    comments: /a^/g, // Remove all comments
                    compress: {
                        warnings: false
                    }
                })
            ],
            node: {
                __filename: true
            }
        }
    });

    require("grunt-string-replace/tasks/string-replace")(grunt);
    grunt.config.set("string-replace", {
        html: {
            options: {
                replacements: [{
                    pattern: "${build}",
                    replacement: "<%= revision %> <%= grunt.template.today(\"yyyy/mm/dd HH:MM:ss Z\") %>"
                }]
            },
            files: [{
                src: "target/dist/index.html",
                dest: "target/dist/index.html"
            }]
        }
    });

    require("grunt-git-revision/tasks/revision")(grunt);
    grunt.config.set("revision", {
        options: {
            property: "revision"
        }
    });

    require("grunt-contrib-clean/tasks/clean")(grunt);
    grunt.config.set("clean", {
        build: ["target/dist/*"]
    });


    grunt.registerTask("build", [
        "clean:build",
        "webpack:build",
        "revision",
        "string-replace:html"
    ]);
    grunt.registerTask("maven-compile", ["build"]);
};
