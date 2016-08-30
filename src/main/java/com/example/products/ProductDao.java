package com.example.products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

//uses JDBCtemplate to interface with DB
@Repository
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(ProductDao.class);

    public Product insertProduct(Product product){
        log.info("Inserting product " + product.getName());
        String sql = "INSERT INTO PRODUCTS (Name, Description, Price) VALUES (?,?,?)";
        KeyHolder idHolder = new GeneratedKeyHolder();
        int numberOfRows = jdbcTemplate.update((Connection connection) -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setDouble(3, product.getPrice());
                return ps;
        }, idHolder);
        int generatedId = idHolder.getKey().intValue();
        product.setId(generatedId);
        log.info(numberOfRows + " affected");
        log.info("Product succesfully inserted with id: " + String.valueOf(generatedId));
        return product;
    }

    public List<Product> getProducts(){
        log.info("Getting all products");
        String sql = "SELECT * FROM  PRODUCTS";
        return jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareCall(sql);
            }
        }, new ProductRowMapper());
    }

    public void updateProductById(Product product){
        log.info("Updating product with id: " + product.getId());
        String sql = "UPDATE PRODUCTS SET Name = ?, Description = ?, Price = ? WHERE Id = ?";
        int numberOfRows = jdbcTemplate.update((Connection connection) -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setDouble(3, product.getPrice());
                //most important as the WHERE clause is crucial
                ps.setInt(4, product.getId());
                return ps;
        });
        log.info(numberOfRows + " affected");
        log.info("product with id " + product.getId() + " updated");
    }

    public void deleteProductById(int productId) throws DataAccessException{
        log.info("Deleting product with id: " + productId);
        String sql = "DELETE FROM PRODUCTS WHERE Id = ?";
        int numberOfRows = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, productId);
                return ps;
            }
        });
        log.info(numberOfRows + " affected");
        log.info("product with id " + productId + " deleted");
    }
}
