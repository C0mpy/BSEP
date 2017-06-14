angular
    .module("myApp", ['ngResource', 'ngRoute', 'ngCookies', 'restangular', 'lodash', 'directive.g+signin'])
    .config(["$routeProvider", function($routeProvider) {
        $routeProvider
            .when("/", {
                templateUrl: "../login.html",
                controller: "loginController",
                controllerAs: "loginCtrl"
            })
            .when("/monitor/:id",{
                templateUrl:"../monitor.html",
                controller:"monitorController",
                controllerAs:"monitorCtrl"
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