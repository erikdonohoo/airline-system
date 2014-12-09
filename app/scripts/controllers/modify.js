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
		$scope.pilots = [];
		$scope.copilots = [];
		angular.forEach(crew, function (p) {
			if (p.position === 'PILOT') {
				$scope.pilots.push(p);
			} else {
				$scope.copilots.push(p);
			}
		});
	});
	$scope.disable = {};
	$scope.disable.airfare = true;
	$http.get('http://localhost:8080/airlinesystem/v1/flights')
	.success(function (flights) {
		$scope.flights = flights;
		setUp(flights);
	});
	$http.get('http://localhost:8080/airlinesystem/v1/planes')
	.success(function (planes) {
		$scope.planes = planes;
	});
	$http.get('http://localhost:8080/airlinesystem/v1/records')
	.success(function (records) {
		$scope.jobs = [];
		var nums = [];
		angular.forEach(records, function (record) {
			angular.forEach(record.jobs, function (job) {
				if (nums.indexOf(job) === -1) {
					nums.push(job);
					$scope.jobs.push({id: job});
				}
			});
		});
	});

	$scope.addRecord = function (record) {
		record.jobs = [];
		angular.forEach($scope.jobs, function (job) {
			if (job.assigned) record.jobs.push(job.id);
		});
		if (record.aircraft) record.faaId = record.aircraft.id;
		$http.post('http://localhost:8080/airlinesystem/v1/records', record)
		.success(function () {
			$route.reload();
		});
	};

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
		}).error(uhoh);
	};

	$scope.$watch('flight.destCity', function (newVal) {
		if (newVal && newVal !== '' && $scope.flight.depCity && $scope.flight.depCity !== '' && $scope.flights) {
			for (var i = 0; i < $scope.flights.length; i++) {
				var flight = $scope.flights[i];
				if (flight.depCity === $scope.flight.depCity && flight.destCity === $scope.flight.destCity) {
					$scope.flight.mileage = flight.mileage;
					$scope.flight.airfare = flight.airfare;
					$scope.disable.mileage = true;
					$scope.disable.airfare = true;
					return;
				}
			}
		}

		$scope.flight.mileage = '';
		$scope.flight.airfare = '';
		$scope.disable.mileage = false;
	});

	$scope.$watch('flight.mileage', function (mileage) {
		if (mileage != null && $scope.flights) {
			for (var i = 0; i < $scope.flights.length; i++) {
				var flight = $scope.flights[i];
				if (flight.mileage === mileage) {
					$scope.flight.airfare = flight.airfare;
					$scope.disable.airfare = true;
					return;
				}
			}

			$scope.disable.airfare = false;
		}
	});

	$scope.addFlight = function (flight) {
		flight.faaId = flight.aircraft.id;
		flight.copilotId = flight.copilotId.id;
		flight.pilotId = flight.pilotId.id;

		// Check if we are adding a new distance or cost
		flight.newRoute = true;
		flight.newCost = true;
		for (var i = 0; i < $scope.flights.length; i++) {
			var f = $scope.flights[i];
			if (f.mileage === flight.mileage) {
				flight.newCost = false;
			}
			if (f.depCity === flight.depCity && f.destCity === flight.destCity) {
				flight.newRoute = false;
			}
		}
		$http.post('http://localhost:8080/airlinesystem/v1/flights', flight)
		.success(function () {
			$route.reload();
		}).error(uhoh);
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

	function uhoh() {
		// Something went wrong
		console.error('something went wrong');
	}
}]);
