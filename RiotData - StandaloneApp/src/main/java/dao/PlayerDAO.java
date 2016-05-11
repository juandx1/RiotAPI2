package dao;

import models.Builds;
import models.Player;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Juan Manuel on 6/05/2016.
 */
@Singleton
public class PlayerDAO {

	private EntityManager entityManager;

	final Semaphore semaphore = new Semaphore(1);

	public PlayerDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(Player player) {
            try {
                semaphore.acquire();
                System.out.println("Guardo: "+player.getSummonerId() + "-" + player.getPlatformId().getPlatform());
                entityManager.getTransaction().begin();
                entityManager.persist(player);
                entityManager.getTransaction().commit();
                semaphore.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	public Player getById(Long summonerId) {
		return entityManager.find(Player.class, summonerId);
	}

	public List<Player> getAll() {
		Query query = entityManager.createNamedQuery("Player.findAll");
		return query.getResultList();
	}
}
