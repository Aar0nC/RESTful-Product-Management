angular.module('products').controller('productsController', ['$scope', 'productRestFactory', 'dataFactory', function($scope, productRestFactory, dataFactory){
    $scope.product = {};
    $scope.addProductBool = false;
    $scope.editProductBool = false;
    $scope.serverError = false;
    $scope.serverSuccess = false;

    if(dataFactory.getProductCollection().length !== 0){
        //do not make another rest call
        $scope.productCollection = dataFactory.getProductCollection();
    }
    else{
        //call the backend
        productRestFactory.query(function(response){
            console.log(response.length + " products received");
            console.log(response);
            $scope.productCollection = response;
            dataFactory.setProductCollection(angular.copy($scope.productCollection));
        }, function(error){
            console.log(error);
            $scope.serverError = true;
            $scope.error = error;
        });
    }

    $scope.addProduct = function(){
        $scope.addProductBool = true;
    };

    $scope.cancelAddProduct = function(){
        $scope.addProductBool = false;
        $scope.editProductBool = false;
        $scope.product = {};
        $scope.newProduct.$setPristine(true);
        $scope.message = {};
        $scope.serverError = false;
        $scope.serverSuccess = false;
    };

    $scope.editProduct = function(product){
        $scope.editProductBool = true;
        $scope.product = product;
    };


    $scope.submitNewProduct = function(){
        productRestFactory.save($scope.product, function(response){
            console.log("product with id " + response.id + " inserted");
            console.log(response);
            $scope.productCollection.push(response);
            dataFactory.setProductCollection(angular.copy($scope.productCollection));
            $scope.serverSuccess = true;
            $scope.message = "Product " + response.id + " inserted";
        }, function(error){
            console.log(error);
            $scope.serverError = true;
            $scope.message = error;
        });
        $scope.cancelAddProduct();
    };

    $scope.deleteProduct = function(product){
        productRestFactory.remove({id: product.id}, function(response){
            console.log("product deleted");
            console.log(response);
            //remove from the view
            var index = $scope.productCollection.indexOf(product);
            $scope.productCollection.splice(index, 1);
            dataFactory.setProductCollection(angular.copy($scope.productCollection));
            $scope.serverSuccess = true;
            $scope.message = "Product " + product.id + " deleted";
        }, function(error){
            console.log(error);
            $scope.serverError = true;
            $scope.error = error;
        });
        $scope.cancelAddProduct();
    };

    $scope.submitEditProduct = function(){
        productRestFactory.update({id: $scope.product.id}, $scope.product, function(response){
            console.log("product updated");
            console.log(response);
            $scope.serverSuccess = true;
            $scope.message = "Product " + $scope.product.id + " edited";
        },function(error){
            console.log(error);
            $scope.serverError = true;
            $scope.error = error;
        });
        $scope.cancelAddProduct();
    };

}]);