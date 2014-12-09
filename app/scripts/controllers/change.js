'use strict';

angular.module('airlineSystemApp').controller('ChangeCtrl', [
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
	$http.get('http://localhost:8080/airlinesystem/v1/flights')
	.success(function (flights) {
		$scope.flights = flights;
	});
	$http.get('http://localhost:8080/airlinesystem/v1/planes')
	.success(function (planes) {
		$scope.planes = planes;
	});
	$http.get('http://localhost:8080/airlinesystem/v1/records')
	.success(function (records) {
		$scope.records = records;
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
	$scope.modify = {};

	$scope.updateCrew = function (crew) {
		if (crew.sup.id) crew.supervisor = crew.sup.id;
		$http.put('http://localhost:8080/airlinesystem/v1/crew/' + crew.id, crew)
		.success(function () {
			$route.reload();
		});
	};

	$scope.updateFlight = function (flight) {
		if (flight.pilot) flight.pilotId = flight.pilot.id;
		if (flight.copilot) flight.copilotId = flight.copilot.id;
		if (flight.aircraft) flight.faaId = flight.aircraft.id;
		$http.put('http://localhost:8080/airlinesystem/v1/flights/' + flight.id, flight)
		.success(function () {
			$route.reload();
		});
	};

	$scope.deleteFlight = function (flight) {
		$http.delete('http://localhost:8080/airlinesystem/v1/flights/' + flight.id)
		.success(function () {
			$route.reload();
		});
	};

	$scope.startCrewModify = function (crew) {
		angular.forEach($scope.crewmembers, function (c) {
			if (c.id === crew.supervisor) crew.sup = c;
		});
		$scope.modify.crew = crew;
	};

	$scope.modifyRecord = function (record) {
		record.jobs = [];
		angular.forEach($scope.jobs, function (j) {
			if (j.assigned) record.jobs.push(j.id);
		});
		if (record.aircraft) record.faaId = record.aircraft.id;
		$http.put('http://localhost:8080/airlinesystem/v1/records/' + record.id, record)
		.success(function () {
			$route.reload();
		});
	};

	$scope.setRecordModify = function (record) {
		angular.forEach($scope.jobs, function (job) {
			angular.forEach(record.jobs, function (j) {
				if (j === job.id) job.assigned = true;
			});
		});
		record.maintDate = new Date(record.maintDate);
		record.nextDate = new Date(record.nextDate);
		angular.forEach($scope.planes, function (plane) {
			if (plane.id === record.faaId) record.aircraft = plane;
		});
		$scope.modify.record = record;
	};

	$scope.setModifyFlight = function (flight) {
		$scope.modify.flight = flight;
		angular.forEach($scope.planes, function (plane) {
			if (plane.id === flight.faaId) flight.aircraft = plane;
		});
		angular.forEach($scope.pilots, function (c) {
			if (c.id === flight.pilotId) flight.pilot = c;
		});
		angular.forEach($scope.copilots, function (c) {
			if (c.id === flight.copilotId) flight.copilot = c;
		});
	};
}])

.filter('crewName', function () {
	return function (id, crew) {
		if (!id || !crew) return '(none)';
		for (var i = 0; i < crew.length; i++) {
			if (crew[i].id === id) return crew[i].name;
		}
		return '(none)';
	};
});
