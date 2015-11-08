module.controller("Schedule", function($scope, $http, menu, $location, $rootScope, $routeParams, $filter) { 
	var id = $routeParams.schedule;
	var user = $routeParams.user;	
	var accountId = $rootScope.accountId;
	$scope.deleteButtons = false;
	$scope.deleteSlotButtons = false; 
	$scope.deleteSubjectButtons = false; 
	$scope.resourceAlert = false;
	$scope.slotAlert = false;
	$scope.subjectAlert = false;
	$scope.slot = {};
	$scope.generating = false;
	$scope.parameters = true;
	
	$scope.setDayAndHour = function() {
		$scope.slot.hour = 0;
		$scope.slot.day = 0;
		$scope.maxDay = $scope.params.days-1;
		$scope.maxHour = $scope.params.hours;
	};
	
	$http.get('/rest/account/' + $rootScope.name).success(function(data) {
		$rootScope.accountId = data.id;
	});
	
	$scope.deleteSchedule = function() {
		$('#deleteModal').modal('hide');
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
	
	$scope.showParameters = function(){
		$scope.parameters = true;
	};
	
	$scope.hideParameters = function(){
		$scope.parameters = false;
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
			
			$scope.teachers = [];
			$scope.groups = [];
			data.forEach(function(entry) {
			    if (entry.type == 'teacher') {
			    	$scope.teachers.push(entry);
			    } else if (entry.type == 'students') {
			    	$scope.groups.push(entry);
			    }
			});
		});
	};	
	
	$scope.fetchSubjects = function() {
		alert("fetched");
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
			
			$scope.params.hoursA = data.hoursA;
			$scope.params.hoursB = data.hoursB;
			$scope.params.hoursC = data.hoursC;
			$scope.params.hoursD = data.hoursD;
			
			$scope.params.freeA = data.freeA;
			$scope.params.freeB = data.freeB;
			
			$scope.params.hours0 = data.hours0;
			
			$scope.params.crossoverProbability = data.crossoverProbability;
			$scope.params.mutationProbability = data.mutationProbability;
			
			$scope.params.populationSize = data.populationSize;
			$scope.params.iterations = data.iterations;

			$scope.hours();
			$scope.chart();
			$scope.free();

		});
		
	};
	
	$scope.createSlot = function(slot) {
		if (slot != undefined && slot.duration != undefined && (slot.students != undefined || slot.teacher != undefined)) {
			
			if (slot.isFixed == undefined) {
				slot.isFixed = false;
			}
			var slotToSave = slot;
				$http.post('/rest/'+ $rootScope.accountId +'/schedules/' + id + '/slots', slot).success(function() {
					$scope.fetchSlots();
					$scope.slotAlert = false;
					$('#slotModal').modal('hide');
				});
		} else {
			$scope.slotAlert = true;
		}
	};
	
	$scope.createRes = function(res) {
		if (res != undefined && res.name != undefined && res.type != undefined && res.name.length > 0) {
				$http.post('/rest/'+ $rootScope.accountId +'/schedules/' + id + '/resources', res).success(function() {
					$scope.fetchResources();
					$scope.resourceAlert = false;
					$('#resModal').modal('hide');
				});	
		} else {
			$scope.resourceAlert = true;
		}
	};
	
	$scope.createSubject = function(subject) {	
		if (subject != undefined && subject.name != undefined && subject.name.length > 0) {
				$http.post('/rest/'+ $rootScope.accountId +'/schedules/' + id + '/subjects', subject).success(function() {
					$scope.fetchSubjects();
					$scope.subjectAlert = false;
					$('#subjectModal').modal('hide');
				});
		} else {
			$scope.subjectAlert = true;
		}
	};
	
	
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
		$scope.generating = true;
		$http.post('/rest/'+ $rootScope.accountId +'/schedules/'+ id + '/generate', params).success(function(){
			$scope.generating = false;
			$scope.fetchSchedule();
			$scope.fetchResources();
			$scope.fetchSubjects();
			$scope.fetchSlots();
		});
	};
	
	$scope.fetchResources();
	$scope.fetchSubjects();
	$scope.fetchSlots();
	
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
		
	
					var m = [ 10, 30, 30, 30 ];
					var w = parseInt(d3.select("#graph").style("width")) - m[1] - m[3];		
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
	
	function myFunction(lessons) {
		if (lessons == $scope.params.hours0) {
			return 0;
		} else if(lessons > 0 && lessons <= $scope.params.hoursA){
			return 1;
		} else if (lessons > $scope.params.hoursA && lessons <= $scope.params.hoursB) {
			return ($scope.params.hoursB - lessons) / ($scope.params.hoursB - $scope.params.hoursA);
		} else if ($scope.params.hoursB < lessons && lessons <= $scope.params.hoursC) {
			return 0;
		} else if ($scope.params.hoursC < lessons && lessons <= $scope.params.hoursD) {
			return (lessons - $scope.params.hoursC) / ($scope.params.hoursD - $scope.params.hoursC);
		} else {
			return 1;
		}             
	}
	
	$scope.hours = function() {
		$('#hours').empty();

					var lim = 10;
					var data = [];
					
					for(var i = 0; i < lim; i = i + 0.1) {
						data.push(myFunction(i));
					}
		
					var m = [ 10, 40, 30, 60 ];
					var w = parseInt(d3.select("#hours").style("width")) - m[1] - m[3];
					var h = 300 - m[0] - m[2];

					var x = d3.scale.linear().domain([0, data.length]).range([0, w]);
					var y = d3.scale.linear().domain([0, d3.max(data)]).range([h, 0]);
					var line = d3.svg.line()

					.x(function(d,i) { 
						return x(i); 
					})
					.y(function(d) { 
						return y(d); 
					})

					var graph = d3.select("#hours").append("svg:svg").attr("width", w + m[1] + m[3]).attr("height",
							h + m[0] + m[2]).append("svg:g").attr("transform", "translate(" + m[3] + "," + m[0] + ")");

					var xAxis = d3.svg.axis().scale(x).orient("bottom").innerTickSize(-h).outerTickSize(0).tickPadding(10);

					graph.append("svg:g").attr("class", "x axis").attr("transform", "translate(0," + h + ")").call(xAxis);

					var yAxis = d3.svg.axis().scale(y).innerTickSize(-w).outerTickSize(0).tickPadding(10).orient("left");

					graph.append("svg:g").attr("class", "y axis").call(yAxis);

					graph.append("svg:path").attr("class", "line").attr("d",
							line(data));
				
	};
		
	function freeValue(lessons) {
		return $scope.params.freeA*lessons + $scope.params.freeB;           
	}
	
	$scope.free = function() {
		$('#free').empty();

					var lim = 10;
					var data = [];
					
					for(var i = 0; i < lim; i = i + 1) {
						data.push(freeValue(i));
					}
					
					var m = [ 10, 40, 30, 60 ];
					var w = parseInt(d3.select("#free").style("width")) - m[1] - m[3];
					var h = 300 - m[0] - m[2];

					var x = d3.scale.linear().domain([0, data.length]).range([0, w]);
					var y = d3.scale.linear().domain([0, d3.max(data)]).range([h, 0]);
					var line = d3.svg.line()

					.x(function(d,i) { 
						return x(i); 
					})
					.y(function(d) { 
						return y(d); 
					})

					var graph = d3.select("#free").append("svg:svg").attr("width", w + m[1] + m[3]).attr("height",
							h + m[0] + m[2]).append("svg:g").attr("transform", "translate(" + m[3] + "," + m[0] + ")");

					var xAxis = d3.svg.axis().scale(x).orient("bottom").innerTickSize(-h).outerTickSize(0).tickPadding(10);

					graph.append("svg:g").attr("class", "x axis").attr("transform", "translate(0," + h + ")").call(xAxis);

					var yAxis = d3.svg.axis().scale(y).innerTickSize(-w).outerTickSize(0).tickPadding(10).orient("left");

					graph.append("svg:g").attr("class", "y axis").call(yAxis);

					graph.append("svg:path").attr("class", "line").attr("d",
							line(data));
				
	};	
});