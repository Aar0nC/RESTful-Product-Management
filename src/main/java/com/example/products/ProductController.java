package com.example.products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {

    //TODO if performance is an issue turn on cache header
    //TODO change return type to ResponseEntity<T> to provide custom response codes
    //TODO define customized @ResponseStatus exceptions
    //TODO endpoint authentication
    //TODO peform server side validation as client side validation can be bypassed.

    @Autowired
    private ProductDao productDao;

    private static  final Logger log = LoggerFactory.getLogger(ProductController.class);

    @RequestMapping(value = "/product", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public Product insertProduct(@RequestBody Product product, HttpServletResponse httpServletResponse) throws IOException {
        log.info("POST /product endpoint reached");
        if (!validateProductRequestBody(product)) {
            log.info("Invalid product request body. Not proceeding with request");
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "product is invalid: fields cannot be null");
            return null;
        }
        return productDao.insertProduct(product);
    }

    @RequestMapping(value = "/product", method=RequestMethod.GET, produces="application/json")
    public List<Product> getAllProducts(HttpServletResponse httpServletResponse) throws IOException{
        log.info("GET /product endpoint reached");
        return productDao.getProducts();
    }

    @RequestMapping(value = "/product/{id}", method=RequestMethod.PUT, consumes="application/json")
    public void updateProduct(@RequestBody Product product, HttpServletResponse httpServletResponse) throws IOException{
        //here the @PathVariable id does not suffice, we need the whole product to update.
        log.info("PUT /product/:id endpoint reached");
        if (!validateProductRequestBody(product)) {
            log.info("Invalid product request body. Not proceeding with request.");
            httpServletResponse.sendError(HttpServletResponse.SC_PARTIAL_CONTENT, "product is invalid: fields cannot be null");
            return;
        }
        productDao.updateProductById(product);
    }

    @RequestMapping(value = "/product/{id}", method=RequestMethod.DELETE)
    public void deleteProduct(@PathVariable int id){
        log.info("DELETE /product/:id endpoint reached");
        productDao.deleteProductById(id);
    }

    private boolean validateProductRequestBody(Product product) {
        //server-side validation of the product object.
        //according to database constraints (null and VARCHAR sizes)
        return product.getName() != null && product.getName().length() <= 100
                && product.getDescription() != null && product.getDescription().length() <= 500;
    }
}
