/*global module*/
module.exports = function (grunt) {
    "use strict";

    /**
     * Tasks for producing the final build output.
     */

    grunt.loadNpmTasks("grunt-requirejs");
    grunt.config.set("requirejs", {
        options: {
            mainConfigFile: "../../common/common-client/src/main/javascript/require.config.js",
            findNestedDependencies: true,
            almond: true,
            optimize: "uglify2",
            preserveLicenseComments: false,
            generateSourceMaps: true
        },
        build: {
            options: {
                // The starting slash here seems to be critical.
                name: "/todo/todo-client/src/main/javascript/main.js",
                out: "target/dist/main.js"
            }
        }
    });

    grunt.loadNpmTasks("grunt-contrib-concat");
    grunt.config.set("concat", {
        vendorcss: {
            options: {
                process: function (src, path) {
                    // Rewrite references to other files in the CSS to their new location.
                    return src.replace(/url\(\'?(.*?)(?:(?:\?|\#).*?)?\'?\)/g, function (url, filePath) {
                        var newFilePath = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length);
                        var srcPath = path.substring(0, path.lastIndexOf("/")) + "/" + filePath;

                        // And copy those files to that location.
                        grunt.file.copy(srcPath, "target/dist/vendor/" + newFilePath);

                        return url.replace(filePath, "vendor/" + newFilePath);
                    });
                }
            },
            src:  ["../../common/common-client/bower_components/bootstrap/dist/css/bootstrap.min.css",
                   "../../common/common-client/bower_components/font-awesome/css/font-awesome.min.css",
                   "../../common/common-client/bower_components/Ladda/dist/ladda-themeless.min.css"],
            dest: "target/dist/vendor.css"
        }
    });

    grunt.loadNpmTasks("grunt-string-replace");
    grunt.config.set("string-replace", {
        html: {
            options: {
                replacements: [{
                    pattern: "${build}",
                    replacement: "<%= revision %> <%= grunt.template.today(\"yyyy/mm/dd HH:MM:ss Z\") %>"
                }, {
                    pattern: /\s*<!-- \${css-start}[\S\s]*?\${css-end} -->/,
                    replacement: "\n\t\t<link rel=\"stylesheet\" href=\"vendor.css?v=<%= revision %>\">"
                }, {
                    pattern: /\s*<!-- \${scripts-start}[\S\s]*?\${scripts-end} -->/,
                    replacement: "\n\t\t<script src=\"main.js?v=<%= revision %>\"></script>"
                }]
            },
            files: [{
                src: "src/main/javascript/index.html",
                dest: "target/dist/index.html"
            }]
        }
    });

    grunt.loadNpmTasks("grunt-git-revision");
    grunt.config.set("revision", {
        options: {
            property: "revision"
        }
    });

    grunt.loadNpmTasks("grunt-contrib-clean");
    grunt.config.set("clean", {
        build: ["target/dist/*"]
    });


    grunt.registerTask("build", [
        "clean:build",
        "requirejs:build",
        "concat:vendorcss",
        "revision",
        "string-replace:html"
    ]);
    grunt.registerTask("maven-compile", ["build"]);
};
