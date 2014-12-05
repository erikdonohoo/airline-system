'use strict';

angular.module('airlineSystemApp').controller('FareCtrl', [
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

		// 3. These are all the matching connections, find cheapest
		var connections = FlightService.findConnections(form, $scope.flights);
		angular.forEach(connections, function (route) {
			route.cost = 0;
			angular.forEach(route, function (f) {
				route.cost += f.airfare;
			});
		});

		$scope.cheapestFare = null;
		if (connections.length) {
			$scope.cheapestFare = connections[0];
			angular.forEach(connections, function (route) {
				if (route.cost < $scope.cheapestFare.cost) $scope.cheapestFare = route;
			});
		}

		// Handle options
		if ($scope.cheapestFare.length) {
			if (form.totalStops != null) {
				$scope.cheapestFare = ($scope.cheapestFare.length <= form.totalStops + 1) ? $scope.cheapestFare : null;
				if (!$scope.cheapestFare) return;
			}
			if (form.layover != null) {
				for (var i = 0, j = 1; j < $scope.cheapestFare.length; i++, j++) {
					var flight1 = $scope.cheapestFare[i];
					var flight2 = $scope.cheapestFare[j];
					if (FlightService.timeDiff(flight2.depTime, flight1.arrTime) > (form.layover)) {
						$scope.cheapestFare = null;
						return;
					}
				}
			}
			if (form.stopCity && form.stopCity !== '') {
				angular.forEach($scope.cheapestFare, function (f) {
					if (f.destCity === form.stopCity) {
						$scope.cheapestFare = null;
					}
					if (!$scope.cheapestFare) return;
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

}])

.filter('departure', function () {
	return function (list, dep) {
		if (!list || !dep) return list;

		var l = [];
		angular.forEach(list, function (city) {
			if (dep !== city) l.push(city);
		});
		return l;
	};
})

.filter('stop', function () {
	return function (list, form) {
		if (!list || !form.depCity || !form.destCity) return list;

		var l = [];
		angular.forEach(list, function (city) {
			if (form.depCity !== city && form.destCity !== city) l.push(city);
		});
		return l;
	};
});
