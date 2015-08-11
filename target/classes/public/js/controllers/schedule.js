module.controller("Schedule", function($scope, $http, menu, $location, $rootScope, $routeParams, $filter) { 
	
	var id = $routeParams.schedule;
	var user = $routeParams.user;	
	var accountId = $rootScope.accountId;
	$scope.deleteButtons = false;
	$scope.deleteSlotButtons = false; 
	$scope.deleteSubjectButtons = false; 
	
	$scope.deleteSchedule = function() {
		$http.delete('/rest/'+ $rootScope.accountId +'/schedules/' + $scope.schedule.id).success(function() {
			$location.path('/user/' + $routeParams.user);
		});
	};
	
	$scope.deleteSlot = function(slot) {
		$http.delete('/rest/'+ $rootScope.accountId +'/schedules/' + $scope.schedule.id + '/slots/' + slot.id).success(function() {
			$scope.fetchSlots();
		});
	};
	
	$scope.deleteSubject = function(subject) {
		$http.delete('/rest/'+ $rootScope.accountId +'/schedules/' + $scope.schedule.id + '/subjects/' + subject.id).success(function() {
			$scope.fetchSubjects();
		});
	};
	
	$scope.showDeleteButtons = function(){
		$scope.deleteButtons = true;
	};
	
	$scope.hideDeleteButtons = function(){
		$scope.deleteButtons = false;
	};
	
	$scope.showSlotDeleteButtons = function(){
		$scope.deleteSlotButtons = true;
	};
	
	$scope.hideSlotDeleteButtons = function(){
		$scope.deleteSlotButtons = false;
	};
	
	$scope.showSubjectDeleteButtons = function(){
		$scope.deleteSubjectButtons = true;
	};
	
	$scope.hideSubjectDeleteButtons = function(){
		$scope.deleteSubjectButtons = false;
	};
	
	$scope.fetchResources = function() {
		$http.get('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/resources').success(function(data){
			$scope.resources = data;
		});
	};	
	
	$scope.fetchSubjects = function() {
		$http.get('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/subjects').success(function(data){
			$scope.subjects = data;
		});
	};	
	
	$scope.deleteResource = function(resource) {
		$http.delete('/rest/'+ $rootScope.accountId +'/schedules/' + $scope.schedule.id + '/resources/' + resource.id).success(function() {
			$scope.fetchResources();
		});
	};
	
	$scope.params = {};
	$scope.tab = {};
	$scope.tab.active = 'summary';
	
	$scope.fetchSchedule = function() {
		$http.get('/rest/'+ $rootScope.accountId +'/schedules/'+ id).success(function(data){
			$scope.schedule = data;
			$scope.params.days = data.days;
			$scope.params.hours = data.hours;
			$scope.params.populationSize = data.populationSize;
			$scope.params.iterations = data.iterations;
			$scope.chart();
		});
		
	};
	
	$scope.createSlot = function(slot) {
		$http.post('/rest/'+ $rootScope.accountId +'/schedules/' + id + '/slots', slot).success(function() {
			$scope.fetchSlots();
		});
	};
	
	$scope.createRes = function(res) {
		$http.post('/rest/'+ $rootScope.accountId +'/schedules/' + id + '/resources', res).success(function() {
			$scope.fetchResources();
		});
	};
	
	$scope.createSubject = function(subject) {
		$http.post('/rest/'+ $rootScope.accountId +'/schedules/' + id + '/subjects', subject).success(function() {
			$scope.fetchSubjects();
		});
	};
	
	$scope.fetchSchedule();
	
	var orderBy = $filter('orderBy');

	$scope.order = function(predicate, reverse) {
		$scope.slots = orderBy($scope.slots, predicate, reverse);
	};
	$scope.order('-day',false);
	
	$scope.fetchGrid = function(resId) {
		$http.get('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/resources/'+ resId).success(function(data){
			$scope.grid = data;
			$scope.activeElement = resId;
		});
	};
	
	$scope.fetchSlots = function() {
		$http.get('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/slots').success(function(data){
			$scope.slots = data;
		});
	};
	
	$scope.fetchSubjects = function() {
		$http.get('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/subjects').success(function(data){
			$scope.subjects = data;
		});
	};
	
	
	$scope.generate = function(params) {
		$http.post('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/generate', params).success(function(){
			$scope.fetchSchedule();
		});
	};
	
	
	$scope.drawChart = function(){
		$http.get('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/rates').success(function(data){
			$scope.rates = data;
			$('#div_report').empty();
			var xy = [[]];
			
			for (i = 0; i < $scope.rates.length; ++i) {
				xy[i] = [i, $scope.rates[i]];
			}
			
			$.jqplot('div_report', [ xy ], {
				axes : {
					xaxis : {
						min : 0,
						max : $scope.rates.length-1
					},
					yaxis : {
						min : 0,
						max : 30
					}
				},
				
				grid: {
				        background: '#FFFFFF'
				}, 
				
				seriesDefaults: {
					showMarker: false
				}

			});
		});
	};
	
	$scope.chart = function() {
		$('#graph').empty();
		$http.get('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/rates').success(function(data){

					var m = [ 10, 40, 30, 60 ];
					var w = 800 - m[1] - m[3];
					var h = 400 - m[0] - m[2];

					var x = d3.scale.linear().domain([0, data.length]).range([0, w]);
					var y = d3.scale.linear().domain([0, d3.max(data)]).range([h, 0]);
					var line = d3.svg.line()

					.x(function(d,i) { 
						return x(i); 
					})
					.y(function(d) { 
						return y(d); 
					})

					var graph = d3.select("#graph").append("svg:svg").attr("width", w + m[1] + m[3]).attr("height",
							h + m[0] + m[2]).append("svg:g").attr("transform", "translate(" + m[3] + "," + m[0] + ")");

					var xAxis = d3.svg.axis().scale(x).orient("bottom").innerTickSize(-h).outerTickSize(0).tickPadding(10);

					graph.append("svg:g").attr("class", "x axis").attr("transform", "translate(0," + h + ")").call(xAxis);

					var yAxis = d3.svg.axis().scale(y).innerTickSize(-w).outerTickSize(0).tickPadding(10).orient("left");

					graph.append("svg:g").attr("class", "y axis").call(yAxis);

					graph.append("svg:path").attr("class", "line").attr("d",
							line(data));
				});
	};
});