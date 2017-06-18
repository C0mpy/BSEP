(function() {
    angular.module("myApp").controller("monitorController", monitorController);
    angular.module("myApp").filter("log", function() {
        return function(input, scope) {
            result = [];
            angular.forEach(input, function(l){
                var flag = true;
                angular.forEach(scope.log_structure, function(a) {
                    if (scope[a] !== undefined &&
                        (l["data"][a].toLowerCase()).indexOf(scope[a].toLowerCase()) === -1) {
                        flag = false;
                    }
                });
                if(scope["type"] !== undefined &&
                    (l["type"].toLowerCase()).indexOf(scope["type"].toLowerCase()) === -1)
                    flag = false;
                if(scope["type"] !== undefined &&
                    (l["logName"].toLowerCase()).indexOf(scope["logName"].toLowerCase()) === -1)
                    flag = false;
                if(flag)
                    result.push(l);
            });
            return result;
        };
    });

    function monitorController($routeParams, $http) {
        var self = this;
        self.logDetails = logDetails;
        self.logView = false;
        
        self.logs = [];

        $http.get('api/operator/getLogsByMonitor/'+$routeParams.id).then(function(response){
            self.logs=response.data;
            self.log_structure=self.logs[0].structure.split(" ").slice(0, -1);

        });
        
        function logDetails(log){
        	self.log = log;
        	self.logView = true;
        }
    }

})();