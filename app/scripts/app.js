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
}]);
