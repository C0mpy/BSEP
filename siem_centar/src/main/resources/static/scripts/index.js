(function() {
    angular.module("myApp").controller("indexController", indexController);

    function indexController() {
        var vm = this;

        console.log("is sunny here in indexCtrl!");
    }
})();