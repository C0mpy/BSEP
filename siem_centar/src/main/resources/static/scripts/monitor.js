(function() {
    angular.module("myApp").controller("monitorController", monitorController);

    function monitorController($routeParams,$http, $cookies, $scope) {
        var self=this;

        self.logs=[]

        $http.get('api/operator/getLogsByMonitor/'+$routeParams.id).then(function(response){
            self.logs=response.data
            self.log_structure=self.logs[0].structure.split(" ");

        })

    }

})();