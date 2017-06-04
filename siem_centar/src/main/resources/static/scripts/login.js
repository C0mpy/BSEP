(function() {
    angular.module("myApp").controller("loginController", loginController);

    function loginController($http) {
        var vm = this;

        vm.login = login;

        function login() {
            console.log("todo");
        }


    }
})();