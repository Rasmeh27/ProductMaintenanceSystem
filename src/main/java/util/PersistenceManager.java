package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProductsPU");

   private PersistenceManager() {}

    public static EntityManager getEntityManager() {
       return emf.createEntityManager();
    }

    public static void close() {
       if (emf != null && emf.isOpen()) {
           emf.close();
       }
    }
}
