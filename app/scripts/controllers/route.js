'use strict';

angular.module('airlineSystemApp').controller('RouteCtrl', [
	'$scope',
	'$http',
	'FlightService',
function ($scope, $http, FlightService) {
	$http.get('http://localhost:8080/airlinesystem/v1/flights')
	.success(function (flights) {
		setUp(flights);
	});

	$scope.findFare = function (form) {
		$scope.cheapeastFare = {};

		// These are all the matching connections, find shortest
		var connections = FlightService.findConnections(form, $scope.flights);
		angular.forEach(connections, function (route) {
			route.distance = 0; // Find total distance
			route.timeWOLayover = 0; // Find time without layover
			angular.forEach(route, function (f) {
				route.distance += f.mileage;
				route.timeWOLayover += FlightService.timeDiff(f.arrTime, f.depTime);
			});
			route.timeWLayover = 0;
			for (var i = 0, j = 1; j < route.length; i++, j++) {
				var flight1 = route[i];
				var flight2 = route[j];
				route.timeWLayover += FlightService.timeDiff(flight2.depTime, flight1.arrTime);
			}
			route.timeWLayover += route.timeWOLayover;
		});

		$scope.bestMatch = connections[0] || null;

		if ($scope.bestMatch) {
			if (form.value === 1) {
				// Shortest distance
				angular.forEach(connections, function (route) {
					if (route.distance < $scope.bestMatch.distance) {
						$scope.bestMatch = route;
					}
				});
			} else if (form.value === 2) {
				// Minimal layover
				angular.forEach(connections, function (route) {
					if (route.length < $scope.bestMatch.length) {
						$scope.bestMatch = route;
					}
				});
			} else if (form.value === 3) {
				// Shortest flight time
				angular.forEach(connections, function (route) {
					if (route.timeWOLayover < $scope.bestMatch.timeWOLayover) {
						$scope.bestMatch = route;
					}
				});
			} else if (form.value === 4) {
				// Shortest total time including layover
				angular.forEach(connections, function (route) {
					if (route.timeWLayover < $scope.bestMatch.timeWLayover) {
						$scope.bestMatch = route;
					}
				});
			}
		}
	};

	function setUp(flights) {
		$scope.flights = flights;
		$scope.cities = [];
		angular.forEach(flights, function (f) {
			if ($scope.cities.indexOf(f.depCity) === -1) {
				$scope.cities.push(f.depCity);
			}
			if ($scope.cities.indexOf(f.destCity) === -1) {
				$scope.cities.push(f.destCity);
			}
		});
	}

}]);
