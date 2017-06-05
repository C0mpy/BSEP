(function() {
    angular.module("myApp").controller("loginController", loginController);

    function loginController($http, $scope) {
        var vm = this;

        vm.login = login;

        function login(data) {
            console.log(data);
        }

        $scope.$on("event:google-plus-signin-success", function (event, authResult) {
            var url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + authResult["access_token"];
            $http.get(url).then(function(response) {
                var data = {"email" : response.data.email, "password" : response.data.id};
                $http.post("api/operator/login", data).then(function(response) {
                   console.log("U LOGGDDMOTHAFACUAA");
                });
            });
        });
        $scope.$on('event:google-plus-signin-failure', function (event,authResult) {
            // Auth failure or signout detected
        });

    }
})();