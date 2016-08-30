angular.module('products').factory('productRestFactory', ['$resource', function($resource){
    //factory function for our service instance that will be injected into controllers
    return $resource('/product/:id', {id: '@_id'}, { update: {method: 'PUT'}});
}]);
