package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import dao.PlayerDAO;
import dao.RegionDAO;
import dao.TopChampionsDAO;
import dto.*;
import models.Region;
import models.Topchampions;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import views.html.index;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private WSClient wsClient;
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("riotPersistenceUnit");

    public static final String API_KEY = "api_key=c23d24ee-d074-4000-a7f0-4711741570c4";

    private PlayerDAO playerDAO;
    private TopChampionsDAO topChampionsDAO;
    private RegionDAO regionDAO;

    @Inject
    public HomeController(ActorSystem actorSystem, ExecutionContextExecutor exec, WSClient wsClient) {
        this.wsClient = wsClient;
        playerDAO = new PlayerDAO(entityManagerFactory.createEntityManager());
        topChampionsDAO = new TopChampionsDAO(entityManagerFactory.createEntityManager());
        regionDAO = new RegionDAO(entityManagerFactory.createEntityManager());
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result index(long summonerId, String region) {
        Region actualRegion = regionDAO.getById(region);
        SummonerDTO summonerDTO = new SummonerDTO();
        summonerDTO.summonerId = summonerId;
        String URL = actualRegion.getHost() + "championmastery/location/" + actualRegion.getPlatformId() + "/player/" + summonerId + "/topchampions?count=4&" + API_KEY;
        CompletionStage<JsonNode> masteryStats = wsClient.url(URL).get().thenApply(WSResponse::asJson);
        List<ChampionMasteryDTO> topchampions = new ArrayList<>();
        masteryStats.handle((jsonNode, throwable) -> {
            String rankedUrl = actualRegion.getHost() + "api/lol/" + actualRegion.getPlatform() + "/v1.3/stats/by-summoner/" + summonerId + "/ranked?season=SEASON2016&" + API_KEY;
            CompletionStage<JsonNode> rankedStats = wsClient.url(rankedUrl).get().thenApply(WSResponse::asJson);
            rankedStats.handle((jsonNode1, throwable1) -> {
                RankedStatsDTO rankedStatsDTO = Json.fromJson(jsonNode1, RankedStatsDTO.class);
                Iterator<JsonNode> jsonNodeIterator = jsonNode.elements();
                while (jsonNodeIterator.hasNext()) {
                    JsonNode node = jsonNodeIterator.next();
                    ChampionMasteryDTO championMasteryDTO = Json.fromJson(node, ChampionMasteryDTO.class);
                    ChampionStatsDTO championStatsDTO = null;
                    for (int i = 0; i < rankedStatsDTO.champions.size() && championStatsDTO == null; i++) {
                        if (rankedStatsDTO.champions.get(i).id == championMasteryDTO.championId) {
                            championStatsDTO = rankedStatsDTO.champions.get(i);
                        }
                    }
                    if (championStatsDTO != null) {
                        topchampions.add(championMasteryDTO);
                    }
                }
                String urlRecent = actualRegion.getHost() + "api/lol/" + actualRegion.getPlatform() + "/v1.3/game/by-summoner/" + summonerId + "/recent?" + API_KEY;
                CompletionStage<JsonNode> recent = wsClient.url(urlRecent).get().thenApply(WSResponse::asJson);
                recent.handle((jsonNode2, throwable2) -> {
                    RecentGamesDTO recentGamesDTO = Json.fromJson(jsonNode2, RecentGamesDTO.class);
                    for (GameDTO gameDTO : recentGamesDTO.games) {
                        ChampionMasteryDTO topchampions1 = null;
                        for (int j = 0; j < topchampions.size() && topchampions1 == null; j++) {
                            if (topchampions.get(j).championId == gameDTO.championId) {
                                topchampions1 = topchampions.get(j);
                            }
                        }
                    }
                    return "";
                });
                return "";
            });
            return "";
        });

        return ok(index.render(""));
    }

}
