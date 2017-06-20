(function() {
    angular.module("myApp").controller("alarmController", alarmController);

    function alarmController($http, $cookies, $location) {

        var vm = this;

       vm.get_alarms=get_alarms;
       vm.addAlarm=addAlarm;

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

                        });
                    })

        });

        function get_alarms(){
            if(vm.agent_id && vm.monitor_id){
                $http.get('api/operator/getAlarms/'+vm.agent_id+"/"+vm.monitor_id).then(function(response){

                    vm.alarms=response.data;
                })
            }else{
                con("parameters not selected");
            }
        }
        
        function addAlarm(){
        
        	$http.post("/api/admin/addAlarm", {"response": "\n\n"+vm.rule}).then(function(response) {
        		
        		vm.rule = "";
        		alert("New Alarm Rule added!");
               
            });
        }


    }


})();