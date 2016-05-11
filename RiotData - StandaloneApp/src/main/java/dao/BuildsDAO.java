package dao;

import models.Builds;
import models.BuildsPK;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Juan Manuel on 6/05/2016.
 */
@Singleton
public class BuildsDAO {

    private EntityManager entityManager;

    public BuildsDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Builds builds) {
        entityManager.getTransaction().begin();
            entityManager.persist(builds);
        entityManager.getTransaction().commit();
    }

    public void edit(Builds builds) {
        entityManager.getTransaction().begin();
            entityManager.merge(builds);
        entityManager.getTransaction().commit();
    }

    public void delete(Builds builds) {
        entityManager.getTransaction().begin();
            entityManager.remove(builds);
        entityManager.getTransaction().commit();
    }

    public Builds getById(BuildsPK buildsPK) {
        entityManager.getTransaction().begin();
        return entityManager.find(Builds.class, buildsPK);
    }

    public List<Builds> getAll() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createNamedQuery("Builds.findAll");
        return query.getResultList();
    }
}
