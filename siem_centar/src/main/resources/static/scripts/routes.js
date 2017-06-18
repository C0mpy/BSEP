angular
    .module("myApp", ['ngResource', 'ngRoute', 'ngCookies', 'restangular', 'lodash',
        'directive.g+signin', 'ui.bootstrap'])
    .config(["$routeProvider", function($routeProvider) {
        $routeProvider
            .when("/", {
                templateUrl: "../login.html",
                controller: "loginController",
                controllerAs: "loginCtrl"
            })
            .when("/agent/", {
                templateUrl: "../agent.html",
                controller: "agentController",
                controllerAs: "agentCtrl"
            })
            .when("/monitor/:id", {
                templateUrl: "../monitor.html",
                controller: "monitorController",
                controllerAs: "monitorCtrl"
            })
            .otherwise({
                redirectTo: "/"
            });
    }])
    .run(['Restangular', '$log', function(Restangular, $log) {
        Restangular.setErrorInterceptor(function (response) {
            if (response.status === 500) {
                $log.info("internal server error");
            }
            return true;
        });
    }]);