'use strict';

angular.module('airlineSystemApp').controller('NavCtrl', [
	'$scope',
	'$location',
function ($scope, $location) {
	$scope.$location = $location;
}]);
