module.controller("User", function($scope, $http, menu, $rootScope, $window, $location) {
		
	$scope.scheduleAlert = false;
	
	$scope.info = "schedules list";
	$scope.fetchSchedules = function() {
		$http.get('/rest/'+ $rootScope.accountId +'/schedules').success(function(data){
			$scope.schedules = data;
		});
	};
	
	$scope.createSchedule = function(schedule) {
		if (schedule != undefined && schedule.name != undefined && schedule.name.length > 0) {			
			$http.post('/rest/'+ $rootScope.accountId +'/schedules', schedule).success(function() {
				$scope.fetchSchedules();	
				$scope.scheduleAlert = false;
				$('#scheduleModal').modal('hide');
			});
		} else {
			$scope.scheduleAlert = true;
		}
	};
	
	$http.get('/rest/account/' + $rootScope.name).success(function(data) {
		$rootScope.accountId = data.id;
		$scope.fetchSchedules();
	});
	
	$scope.setSchedule = function(schedule) {
		$rootScope.activeSchedule = schedule;
	};
});