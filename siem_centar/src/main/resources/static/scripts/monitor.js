(function() {
    angular.module("myApp").controller("monitorController", monitorController);
    
    function monitorController($routeParams,$http, $cookies, $scope) {
        var self=this;
        self.logDetails = logDetails;
        
        self.logs=[]

        $http.get('api/operator/getLogsByMonitor/'+$routeParams.id).then(function(response){
            self.logs=response.data;
            self.log_structure=self.logs[0].structure.split(" ").slice(0, -1);

        })
        
        function logDetails(log){
        	alert(JSON.stringify(log));
        	self.log = log;
        	   	
        }
        
       

    }
    
    

})();