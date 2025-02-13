package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Categories;
import models.Products;
import util.PersistenceManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/productos/*")
public class ProductServlet extends HttpServlet {
    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("ProductsPU");
    }

    //GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        EntityManager em = PersistenceManager.getEntityManager();

        try {
            em.getTransaction().begin();
            em.clear();  // Asegura que Hibernate obtenga los datos actualizados

            List<Products> products = em.createQuery(
                            "SELECT p FROM Products p WHERE p.status = true", Products.class)
                    .getResultList();

            em.getTransaction().commit();

            response.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), products);
        } catch (Exception e) {
            em.getTransaction().rollback();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener productos: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    //DELETE
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto no proporcionado");
            return;
        }

        Long id = Long.parseLong(pathInfo.substring(1));
        EntityManager em = PersistenceManager.getEntityManager();

        try {
            em.getTransaction().begin();
            Products product = em.find(Products.class, id);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado");
                return;
            }

            // Inhabilitar el producto en lugar de eliminarlo
            product.setStatus(false);
            em.merge(product);

            em.getTransaction().commit();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Producto inhabilitado correctamente\"}");
        } catch (Exception e) {
            em.getTransaction().rollback();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al inhabilitar el producto: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    //POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        ObjectMapper mapper = new ObjectMapper();
        Products products = mapper.readValue(request.getReader(), Products.class);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(products);
        em.getTransaction().commit();
        em.close();

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), products);
    }

    //PUT
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("PUT recibido en ProductServlet");  // Log para verificar
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto no proporcionado");
            return;
        }


        Long id = Long.parseLong(pathInfo.substring(1));

        ObjectMapper mapper = new ObjectMapper();
        Products updatedProduct = mapper.readValue(request.getReader(), Products.class);

        EntityManager em = PersistenceManager.getEntityManager();
        try {
            em.getTransaction().begin();
            Products existingProduct = em.find(Products.class, id);
            if (existingProduct == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado");
                return;
            }

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());
            existingProduct.setStatus(updatedProduct.getStatus());
            existingProduct.setCategorie(updatedProduct.getCategorie());

            em.merge(existingProduct);
            em.getTransaction().commit();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            mapper.writeValue(response.getWriter(), existingProduct);
        } catch (Exception e) {
            em.getTransaction().rollback();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en la actualización: " + e.getMessage());
        } finally {
            em.close();
        }
    }

}
