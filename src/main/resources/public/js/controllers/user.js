module.controller("User", function($scope, $http, menu, $rootScope, $window, $location) {
		
	$scope.info = "schedules list";
	$scope.fetchSchedules = function() {
		$http.get('/rest/'+ $rootScope.accountId +'/schedules').success(function(data){
			$scope.schedules = data;
		});
	};
	
	$scope.createSchedule = function(schedule) {
		$http.post('/rest/'+ $rootScope.accountId +'/schedules', schedule).success(function() {
			$scope.fetchSchedules();	
		});
	};
	
	$http.get('/rest/account/' + $rootScope.name).success(function(data) {
		$rootScope.accountId = data.id;
		$scope.fetchSchedules();
	});
	
	$scope.setSchedule = function(schedule) {
		$rootScope.activeSchedule = schedule;
	};
});