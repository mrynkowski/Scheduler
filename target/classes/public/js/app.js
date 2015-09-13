var module = angular.module("app", [ 'ngRoute' ]);
module.config(function($routeProvider, $locationProvider, $httpProvider) {
	$locationProvider.html5Mode(true);
	$routeProvider
	.when('/user/:user/:schedule', {
		templateUrl : '/views/schedule.html',
		controller : 'Schedule'
	}).when('/user/:user', {
		templateUrl : '/views/user.html',
		controller : 'User'
	}).when('/signin', {
		templateUrl : '/views/signin.html',
		controller : 'Navigation'
	}).when('/login', {
		templateUrl : '/views/login.html',
		controller : 'Navigation'
	}).when('/', {
		templateUrl : '/views/home.html',
	});
	
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});

module.factory('menu', function($http, $rootScope) {
	
	var factory = {};
	var items = [];
	
	items.push({text: "Details", link: ""});
	items.push({text: "Resources", link: "Resources"});
	items.push({text: "Slots", link: "Slots"});
	
	factory.items = items;
	return factory;
});
