(function() {
    angular.module("myApp").controller("logController", logController);

    function logController($http, $cookies, $location, $routeParams) {

        var vm = this;
        vm.monitor = $routeParams.id;
    }
})();
