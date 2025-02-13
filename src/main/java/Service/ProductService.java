package Service;

import dao.ProductDAO;
import models.Products;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public void saveProduct(Products product) {
        productDAO.save(product);
    }

    public void updateProduct(Products product) {
        productDAO.update(product);
    }

    public void deleteProduct(Long id) {
        Products product = productDAO.findById(id);
        if (product != null) {
            productDAO.delete(product);
        }
    }

    public Products getProductById(Long id) {
        return productDAO.findById(id);
    }

    public List<Products> getAllProducts() {
        return productDAO.findAll();
    }
}
