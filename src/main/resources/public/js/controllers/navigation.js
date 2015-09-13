module.controller("Navigation", function($rootScope, $scope, $http, $location, $route) {

	$scope.tab = function(route) {
		return $route.current && route === $route.current.controller;
	};

	var authenticate = function(credentials, callback) {

		var headers = credentials ? {
			authorization : "Basic "
					+ btoa(credentials.username + ":"
							+ credentials.password)
		} : {};

		$http.get('/rest/user', {
			headers : headers
		}).success(function(data) {
			if (data.name) {
				$rootScope.name = data.name;
				$rootScope.authenticated = true;
			} else {
				$rootScope.authenticated = false;
			}
			callback && callback($rootScope.authenticated);
		}).error(function() {
			$rootScope.authenticated = false;
			callback && callback(false);
		});

	}

	authenticate();

	$scope.credentials = {};

	$scope.signin = function(account) {
		
		var preparedAccount = {};
		preparedAccount.name = account.username;
		preparedAccount.password = account.password;
		
		$http.post('/rest/signin', preparedAccount).success(function() {
			authenticate($scope.credentials, function(authenticated) {
				if (authenticated) {
					$location.path("/user/"+$rootScope.name);
					$scope.error = false;
					$rootScope.authenticated = true;
				}
			})
		});
		

	};
	
	$scope.login = function() {
		authenticate($scope.credentials, function(authenticated) {
			if (authenticated) {
				$location.path("/user/"+$rootScope.name);
				$scope.error = false;
				$rootScope.authenticated = true;
			} else {
				$location.path("/login");
				$scope.error = true;
				$rootScope.authenticated = false;
			}
		})
	};

	$scope.logout = function() {
		$http.post('/rest/logout', {}).success(function() {
			console.log("Logout succeeded")
			$rootScope.authenticated = false;
			$location.path("/");
		}).error(function(data) {
			console.log("Logout failed")
			$rootScope.authenticated = false;
		});
	}
});