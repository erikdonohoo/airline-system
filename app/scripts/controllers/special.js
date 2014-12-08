'use strict';

angular.module('airlineSystemApp').controller('SpecialCtrl', [
	'$scope',
	'$http',
	'FlightService',
	'$filter',
function ($scope, $http, FlightService, $filter) {
	$http.get('http://localhost:8080/airlinesystem/v1/flights')
	.success(function (flights) {
		setUp(flights);
		$scope.cityForm = {};
	});

	$scope.handleMileage = function (form) {
		$scope.routes = $filter('mileageAndStops')($scope.routeCopy, form);
	};

	$scope.handleAirfare = function (form) {
		$scope.routes = $filter('airfareFilter')($scope.routeCopy, form.airfare);
	};

	$scope.handleArrival = function (form) {
		$scope.routes = $filter('arriveBetween')($scope.routeCopy, form);
	};

	$scope.handlePass = function (dep, through, arr) {
		$scope.passengers = [];
		$http.get('http://localhost:8080/airlinesystem/v1/passengers')
		.success(function (list) {
			checkPass(list);
		});

		function checkPass(pass) {
			// See if any of these passengers start at dep, and arrive at arr and go through through
			angular.forEach(pass, function (passenger) {
				var check1, check2, check3;
				angular.forEach(passenger.flights, function (flightId) {
					angular.forEach($scope.flights, function (flight) {
						if (flight.id === flightId && flight.depCity === dep) check1 = true;
						if (flight.id === flightId && flight.destCity === arr) check3 = true;
						if (flight.id === flightId && (flight.depCity === through || flight.destCity === through)) check2 = true;
					});
				});
				if (check1 && check2 && check3) $scope.passengers.push(passenger);
			});
		}
	};

	function setUp(flights) {
		// Need to find all possible routes
		$scope.routes = [];
		var cities = [];
		$scope.flights = flights;
		angular.forEach(flights, function (f) {
			if (cities.indexOf(f.depCity) === -1) {
				cities.push(f.depCity);
			}
			if (cities.indexOf(f.destCity) === -1) {
				cities.push(f.destCity);
			}
		});
		$scope.cities = cities;
		angular.forEach(flights, function (f) {
			angular.forEach(cities, function (city) {
				if (f.depCity !== city) {
					angular.forEach(FlightService.findConnections({depCity: f.depCity, destCity: city}, flights), function (route) {
						var dup = false;
						angular.forEach($scope.routes, function (goodRoute) {
							if (duplicateRoute(goodRoute, route)) dup = true;
						});
						if (!dup) $scope.routes.push(route);
					});
				}
			});
		});

		// Have all the routes now, add more data
		angular.forEach($scope.routes, function (route) {
			route.mileage = 0;
			route.airfare = 0;
			angular.forEach(route, function (flight) {
				route.mileage += flight.mileage;
				route.airfare += flight.airfare;
			});
			// Add dest, start
			route.depCity = route[0].depCity;
			route.destCity = route[route.length - 1].destCity;
		});

		$scope.routeCopy = $scope.routes;
	}

	function duplicateRoute(route1, route2) {
		if (route1.length !== route2.length) return false;
		for (var i = 0; i < route1.length; i++) {
			var f1 = route1[i];
			var f2 = route2[i];
			if (f1.id !== f2.id) return false;
		}
		return true;
	}

}])

.filter('mileageAndStops', function () {
	return function (list, form) {
		var newlist = [];
		angular.forEach(list, function (route) {
			var add = true;
			if (form.mileage != null) {
				add = (form.mileage <= route.mileage) ? false : add;
			}
			if (form.stops != null) {
				add = (route.length > form.stops + 1) ? false : add;
			}
			if (add) newlist.push(route);
		});
		return newlist;
	};
})

.filter('airfareFilter', function () {
	return function (list, airfare) {
		var newlist = [];
		angular.forEach(list, function (route) {
			if (route.airfare < airfare) newlist.push(route);
		});
		return newlist;
	};
})

.filter('arriveBetween', [function () {
	return function (list, form) {
		var newlist = [];
		angular.forEach(list, function (route) {
			var depMin = parseInt(route[0].depTime.toString().substr(-2), 10);
			var arrMin = parseInt(route[route.length - 1].arrTime.toString().substr(-2), 10);
			var depHr = parseInt(route[0].depTime.toString().substring(0, route[0].depTime.toString().length - 2), 10);
			var arrHr = parseInt(route[route.length - 1].arrTime.toString().substring(0, route[route.length - 1].arrTime.toString().length - 2), 10);

			if (depHr < form.departHour) return;
			if (depHr === form.departHour && depMin < form.departMinute) return;
			if (arrHr > form.arriveHour) return;
			if (arrHr === form.arriveHour && arrMin > form.arriveMinute) return;

			newlist.push(route);
		});
		return newlist;
	};
}])

.filter('fromAndTo', function () {
	function filter(list, form) {
		if (!list || !form || !form.depCity || !form.destCity) return list;
		var newlist = [];
		angular.forEach(list, function (route) {
			if (route.depCity === form.depCity && route.destCity === form.destCity) {
				newlist.push(route);
			}
		});
		return newlist;
	}
	return filter;
});
