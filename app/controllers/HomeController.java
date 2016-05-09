package controllers;

import dao.PlayerDAO;
import dao.RegionDAO;
import dao.TopChampionsDAO;
import models.Player;
import models.Topchampions;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class HomeController extends Controller {

    @Inject
    private WSClient wsClient;
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("riotPersistenceUnit");

    public static final String API_KEY = "api_key=c23d24ee-d074-4000-a7f0-4711741570c4";

    private PlayerDAO playerDAO = new PlayerDAO(entityManagerFactory.createEntityManager());
    private TopChampionsDAO topChampionsDAO = new TopChampionsDAO(entityManagerFactory.createEntityManager());
    private RegionDAO regionDAO = new RegionDAO(entityManagerFactory.createEntityManager());

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok();
    }


    public Result get(Long championId, String region) {
        List<Player> players = playerDAO.getAll();
        for (Player player : players) {
            for (Topchampions topchampion : player.getTopchampionsList()) {

            }
        }
        return ok(index.render(""));
    }
}

