(function() {
    angular.module("myApp").controller("loginController", loginController);

    function loginController($http, $cookies, $scope, $location) {

        var vm = this;

        vm.login = login;

        function login () {
            console.log(vm.email + " : " + vm.pass);
            $http.post("/api/admin/login", {"email": vm.email, "password": vm.pass}).then(function(response) {
                $cookies.put("token", response.data.response);
                // postavlja se token u svaki zahtev
                $http.defaults.headers.common['X-Auth-Token'] = response.data.response;
                $location.path("/alarm");
            });
        };

        // login sa OAuth2 Google button
        $scope.$on("event:google-plus-signin-success", function (event, authResult) {
            var url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + authResult["access_token"];
            $http.get(url).then(function (response) {
                var data = {"email": response.data.email, "password": response.data.id};
                $http.post("/api/operator/login", data).then(function (response) {
                    $cookies.put("token", response.data.response);
                    // postavlja se token u svaki zahtev
                    $http.defaults.headers.common['X-Auth-Token'] = response.data.response;
                    $location.path("/agent");
                });
            });
        });
        $scope.$on('event:google-plus-signin-failure', function (event, authResult) {
            // auth fail
        });

    }
})();
