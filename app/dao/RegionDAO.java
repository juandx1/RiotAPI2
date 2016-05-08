package dao;

import models.Builds;
import models.Region;
import play.db.jpa.JPA;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Juan Manuel on 6/05/2016.
 */
@Singleton
public class RegionDAO {
    private EntityManager entityManager;

    public RegionDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Region getById(String platformId) {
        return entityManager.find(Region.class, platformId);
    }

    public List<Region> getAll() {
        Query query = entityManager.createNamedQuery("Player.findAll");
        return query.getResultList();
    }

}
