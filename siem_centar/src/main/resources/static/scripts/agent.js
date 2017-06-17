(function() {
    angular.module("myApp").controller("agentController", agentController);

    function agentController($http, $cookies, $location) {

        var vm = this;

        vm.getMonitors = getMonitors;
        vm.selectMonitor = selectMonitor;

        $http.get('api/operator/getAllAgents/').then(function(response){
            vm.agents = response.data;
            vm.monitors = {};
            var agent;
            for(agent in vm.agents) {
                vm.monitors[agent] = [];
            }
        });

        function getMonitors(agent) {
            $http.get('api/operator/getMonitors/' + agent)
                .then(function(response) {
                    vm.monitors[agent] = response.data;
                });
        }

        function selectMonitor(monitor) {
            console.log("ere here");
            $cookies.put("monitor", monitor);
            $location.path("/log/" + monitor);
        }

    }



})();