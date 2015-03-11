/*global module, process*/
module.exports = function (grunt) {
    "use strict";

    /**
     * Testing and quality related tasks.
     */

    grunt.loadNpmTasks("grunt-jscs");
    var styleSrc = ["src/**/*.js", "test/**/*.js", "grunt/**/*.js", "Gruntfile.js"];
    grunt.config.set("jscs", {
        options: {
            config: "../../.jscsrc"
        },
        dev: {
            src: styleSrc
        },
        ci: {
            options: {
                reporter: "junit",
                reporterOutput: "target/style.xml"
            },
            src: styleSrc
        }
    });

    grunt.loadNpmTasks("grunt-mocha-test");
    var testSrc = ["src/test/javascript/**/*Test.js"];
    grunt.config.set("mochaTest", {
        options: {
            require: [
                "babel-core/register",
                "./src/test/javascript/testSetup"
            ]
        },
        dev: {
            src: testSrc
        },
        ci: {
            options: {
                reporter: "xunit",
                captureFile: "target/tests.xml",
                quiet: true
            },
            src: testSrc
        }
    });

    grunt.registerTask("coverage", "Generate test coverage report.", function () {
        var istanbulOptions = ["cover", "--root", "./src", "--dir", "./target/coverage", "./node_modules/mocha/bin/_mocha"];
        var mochaOptions = ["--require", "babel-core/register", "--require", "./test/testSetup", /*"--require", "./src/app/Application",*/ "--recursive", "./test"];

        var done = this.async();
        grunt.util.spawn({
            cmd: "node",
            args: ["./node_modules/istanbul/lib/cli"].concat(istanbulOptions).concat("--").concat(mochaOptions),
            opts: {
                env: process.env,
                cwd: process.cwd(),
                stdio: "inherit"
            }
        }, function (err) {
            if (err) {
                grunt.fail.warn(err);
                return;
            }
            done();
        });
    });

    grunt.registerTask("test", ["jscs:dev", "mochaTest:dev"]);
    grunt.registerTask("maven-test", ["jscs:ci", "mochaTest:ci"]);
};
