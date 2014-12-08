'use strict';

angular.module('airlineSystemApp').controller('ModifyCtrl', [
'$scope',
'$http',
'FlightService',
'$route',
function ($scope, $http, FlightService, $route) {
	$http.get('http://localhost:8080/airlinesystem/v1/crew')
	.success(function (crew) {
		$scope.crewmembers = crew;
	});
	$http.get('http://localhost:8080/airlinesystem/v1/flights')
	.success(function (flights) {
		$scope.flights = flights;
		setUp(flights);
	});
	$http.get('http://localhost:8080/airlinesystem/v1/planes')
	.success(function (planes) {
		$scope.planes = planes;
	});

	$scope.addCrew = function (crew) {
		if (crew.supervisor.id) {
			crew.supervisor = crew.supervisor.id;
		} else {
			crew.supervisor = null;
		}

		crew.flights = [];
		angular.forEach($scope.flights, function (flight) {
			if (flight.assigned) crew.flights.push(flight.id);
		});

		$http.post('http://localhost:8080/airlinesystem/v1/crew', crew)
		.success(function () {
			$route.reload();
		});
	};

	function setUp(flights) {
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
