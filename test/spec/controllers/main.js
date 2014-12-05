'use strict';

describe('Controller: MainCtrl', function () {

	// load the controller's module
	beforeEach(module('airlineSystemApp'));

	var MainCtrl,
		scope;

	// Initialize the controller and a mock scope
	beforeEach(inject(['$controller', '$rootScope', function ($controller, $rootScope) {
		scope = $rootScope.$new();
		MainCtrl = $controller('HomeCtrl', {
			$scope: scope
		});
	}]));

	it('should have scope', function () {
		expect(scope).toBeDefined();
	});
});
