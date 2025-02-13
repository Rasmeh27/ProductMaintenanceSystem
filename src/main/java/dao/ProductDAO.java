package dao;

import jakarta.persistence.EntityManager;
import models.Products;
import util.PersistenceManager;

import java.util.List;

public class ProductDAO implements GenericDAO<Products> {

    @Override
    public void save(Products product) {
        EntityManager em = PersistenceManager.getEntityManager();
        em.getTransaction().begin();
        em.persist(product);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(Products product) {
        EntityManager em = PersistenceManager.getEntityManager();
        em.getTransaction().begin();
        em.merge(product);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(Products product) {
        EntityManager em = PersistenceManager.getEntityManager();
        em.getTransaction().begin();
        product.setStatus(false);
        em.merge(product);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Products findById(Long id) {
        EntityManager em = PersistenceManager.getEntityManager();
        Products product = em.find(Products.class, id);
        em.close();
        return product;
    }

    @Override
    public List<Products> findAll() {
        EntityManager em = PersistenceManager.getEntityManager();
        List<Products> products = em.createQuery("SELECT p FROM Products p WHERE p.status = true", Products.class).getResultList();
        em.close();
        return products;
    }


    public List<Products> filterByName(String name) {
        EntityManager em = PersistenceManager.getEntityManager();
        List<Products> products = em.createQuery(
                        "SELECT p FROM Products p WHERE p.name LIKE :name AND p.status = true", Products.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
        em.close();
        return products;
    }

    public List<Products> filterByCategory(Long categoryId) {
        EntityManager em = PersistenceManager.getEntityManager();
        List<Products> products = em.createQuery(
                        "SELECT p FROM Products p WHERE p.categorie.id = :categoryId AND p.status = true", Products.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
        em.close();
        return products;
    }
}



