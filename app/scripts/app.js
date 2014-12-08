'use strict';

angular.module('airlineSystemApp', [
	'ngSanitize',
	'ngRoute'
])

.config(['$routeProvider', function ($routeProvider) {
	$routeProvider.when('/', {
		templateUrl: 'views/home.html',
		controller: 'HomeCtrl'
	})
	.when('/fares', {
		templateUrl: 'views/fares.html',
		controller: 'FareCtrl'
	})
	.when('/routes', {
		templateUrl: 'views/routes.html',
		controller: 'RouteCtrl'
	})
	.when('/special', {
		templateUrl: 'views/special.html',
		controller: 'SpecialCtrl'
	})
	.otherwise({
		redirectTo: '/'
	});
}])

.run(['$rootScope', '$http', function ($scope, $http) {

	// Expose app version info
	$http.get('version.json').success(function (v) {
		$scope.version = v.version;
		$scope.appName = v.name;
	});

	$scope.reset = function () {
		$http.post('http://localhost:8080/airlinesystem/v1/resetdb');
	};
}])

.factory('FlightService', [function () {
	function findRoutes(form, flights) {
		function findConnection (flight, targetCity, list) {
			// For this flight, see what flights we can take (based on depTime, arrTime)
			// And start a bunch more recursive lookups
			// If it finds the target city, stop and add list to connections
			// If it exhausts possible flights, stop
			var flightsToTry = [];
			angular.forEach(flights, function (f) {
				if (f.depTime > flight.arrTime && f.depCity === flight.destCity) {
					flightsToTry.push(f);
				}
			});

			angular.forEach(flightsToTry, function (f) {
				// Check if we have reached destination
				if (f.destCity === targetCity) {
					list.push(f);
					connections.push(list);
				} else {
					var l = angular.copy(list);
					l.push(f);
					findConnection(f, targetCity, l);
				}
			});
		}

		// 1. Find flights that originate from depCity
		var startFlights = [];
		angular.forEach(flights, function (flight) {
			if (flight.depCity === form.depCity) startFlights.push(flight);
		});

		// 2. For each starting flight, find all paths to connecting flights
		var connections = [];
		angular.forEach(startFlights, function (flight) {
			// Are we already there?
			if (flight.destCity === form.destCity) {
				connections.push([flight]);
			} else {
				findConnection(flight, form.destCity, [flight]);
			}
		});

		return connections;
	}

	function timeDiff(time1, time2) {
		// Convert to hours
		var time1min = parseInt(time1.toString().substr(-2), 10);
		var time2min = parseInt(time2.toString().substr(-2), 10);
		var time1hr = parseInt(time1.toString().substring(0, time1.toString().length - 2), 10);
		var time2hr = parseInt(time2.toString().substring(0, time2.toString().length - 2), 10);

		var diffInHrs = 0;
		if (time2min > time1min) {
			diffInHrs += (60 - time2min) / 60;
			time2hr++;
		}
		if (time2min < time1min) {
			diffInHrs += (time1min - time2min) / 60;
		}
		if (time2hr < time1hr) {
			diffInHrs += (time1hr - time2hr);
		}
		return diffInHrs;
	}

	return {
		findConnections: findRoutes,
		timeDiff: timeDiff
	};
}]);
