var require = {
    paths: {
        jquery: "lib/jquery-1.9.1",
        angular: "lib/angular"
    },
    shim: {
        angular: {
            deps: ["jquery"],
            exports: "angular"
        }
    }
};