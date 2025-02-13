package org.example;

import dao.ProductDAO;
import models.Categories;
import models.Products;
import util.PersistenceManager;

import java.util.List;

public class ProductDAOTest {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();

        Categories category = new Categories();
        category.setId(1L);

        category.setName("Electronica");
        category.setDescription("Productos electronicos");

        Products newProduct = new Products();
        newProduct.setName("Iphone 16");
        newProduct.setDescription("Descripcion de prueba");
        newProduct.setPrice(999.99);
        newProduct.setStock(20);
        newProduct.setStatus(true);
        newProduct.setCategorie(category);

        productDAO.save(newProduct);
        System.out.println("Producto guardado con exito: " + newProduct.getName());



//        List<Products> products = productDAO.findAll();
//        System.out.println("Productos activos: ");
//        products.forEach(p -> System.out.println(p.getName() + " - " + p.getDescription()));
//
//        if(!products.isEmpty()) {
//            Products productToDisable = products.get(0);
//            productDAO.delete(productToDisable);
//            System.out.println("Producto inhabilitado: " + productToDisable.getName());
//        }
//
//        List<Products> productsUpdated = productDAO.findAll();
//        System.out.println("Productos activos despues de inhabilitar: ");
//        productsUpdated.forEach(p -> System.out.println(p.getName() + " - " + p.getDescription()));
//
//        PersistenceManager.close();

    }
}
