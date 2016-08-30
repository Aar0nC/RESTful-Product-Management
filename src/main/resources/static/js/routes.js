angular.module('restApp').
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'js/home/home.html'
    }).when('/products', {
        templateUrl: 'js/products/products.html',
        controller: 'productsController'
    }).when('/orders', {
        templateUrl: 'js/orders/orders.html'
        //controller: 'ordersController'
    }).otherwise({
        redirectTo: '/'
    })
}]);
