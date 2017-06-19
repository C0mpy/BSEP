(function() {
    angular.module("myApp").controller("metricsController", metricsController);

    function metricsController($http, $cookies, $location,$scope) {

        var vm = this;
        vm.agent_id="all";
        vm.monitor_id="all";
        vm.date1=new Date();
        vm.date2=new Date();
        vm.log_num="log_num";
        vm.alarm_num="alarm_num";
        vm.get_metrics=get_metrics;

        $http.get('api/operator/getAllAgents/').then(function(response){
                    vm.agents = response.data;
                    vm.monitors = {};
                    var agent;
                    for(agent in vm.agents) {
                        vm.monitors[agent] = [];
                    }

                    vm.agents.forEach(function(item,index){
                        $http.get('api/operator/getMonitors/' + item).then(function(response) {
                            vm.monitors[item] = response.data;

                            console.log(vm.monitors)
                        });
                    })

        });

        function get_metrics(){
            console.log(vm.agent_id + " " + vm.date1  + " " + vm.date2  + " " + vm.monitor_id)

                $http.post("api/operator/getLogNumber", {"agent":vm.agent_id, "monitor":vm.monitor_id,"start":vm.date1,"end":vm.date2}).then(function(response){
                    vm.log_num=response.data
                })


                $http.post("api/operator/getLogNumber", {"agent":vm.agent_id, "monitor":vm.monitor_id,"start":vm.date1,"end":vm.date2}).then(function(response){
                                    vm.alarm_num=response.data
                                })

        }


    }

})();