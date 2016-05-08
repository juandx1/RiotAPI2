package dao;

import models.Builds;
import models.BuildsPK;
import models.Topchampions;
import models.TopchampionsPK;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Juan Manuel on 6/05/2016.
 */
public class TopChampionsDAO {

    private EntityManager entityManager;

    public TopChampionsDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Topchampions topchampions) {
        entityManager.getTransaction().begin();
        entityManager.persist(topchampions);
        entityManager.getTransaction().commit();
    }

    public void edit(Topchampions topchampions) {
        entityManager.getTransaction().begin();
        entityManager.merge(topchampions);
        entityManager.getTransaction().commit();
    }

    public void delete(Topchampions topchampions) {
        entityManager.getTransaction().begin();
        entityManager.remove(topchampions);
        entityManager.getTransaction().commit();
    }

    public Topchampions getById(TopchampionsPK topchampionsPK) {
        return entityManager.find(Topchampions.class, topchampionsPK);
    }

    public List<Topchampions> getAll() {
        Query query = entityManager.createNamedQuery("Topchampions.findAll");
        return query.getResultList();
    }

}
