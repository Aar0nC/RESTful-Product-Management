angular.module('products').factory('dataFactory', function(){
    //factory method that will return our object.
    var productCollection = [];
    return {
        setProductCollection: function (productList) {
            productCollection = productList;
        },
        getProductCollection: function(){
            return productCollection;
        }
    }
});
