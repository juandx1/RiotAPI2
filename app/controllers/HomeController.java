package controllers;

import dao.PlayerDAO;
import dao.RegionDAO;
import dao.TopChampionsDAO;
<<<<<<< HEAD
import dto.*;
import models.Region;
import play.libs.Json;
=======
import models.Player;
import models.Topchampions;
>>>>>>> origin/master
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
<<<<<<< HEAD
import scala.concurrent.ExecutionContextExecutor;
import views.html.home;
=======
import views.html.index;
>>>>>>> origin/master

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
<<<<<<< HEAD
        return ok(home.render());
    }

    public Result index(long summonerId, String region) {
        Region actualRegion = regionDAO.getById(region);
        SummonerDTO summonerDTO = new SummonerDTO();
        summonerDTO.summonerId = summonerId;
        String URL = actualRegion.getHost() + "championmastery/location/" + actualRegion.getPlatformId() + "/player/" + summonerId + "/topchampions?count=4&" + API_KEY;
        CompletionStage<JsonNode> masteryStats = wsClient.url(URL).get().thenApply(WSResponse::asJson);
        List<ChampionDTO> topchampions = new ArrayList<>();
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
                    ChampionDTO championDTO = null;
                    for (int i = 0; i < rankedStatsDTO.champions.size() && championDTO == null; i++) {
                        if (rankedStatsDTO.champions.get(i).id == championMasteryDTO.championId) {
                            championStatsDTO = rankedStatsDTO.champions.get(i);
                            championDTO = new ChampionDTO();
                            championDTO.assits = championStatsDTO.stats.totalAssists;
                            championDTO.championId = championStatsDTO.id;
                            championDTO.deaths = championStatsDTO.stats.totalDeathsPerSession;
                            championDTO.games = championStatsDTO.stats.totalSessionsPlayed;
                            championDTO.gold = championStatsDTO.stats.totalGoldEarned;
                            championDTO.kills = championStatsDTO.stats.totalChampionKills;
                            championDTO.wins = championStatsDTO.stats.totalSessionsWon;
                            championDTO.losses = championStatsDTO.stats.totalSessionsLost;
                        }
                    }
                    if (championDTO != null) {
                        topchampions.add(championDTO);
                    }
                }
                String urlRecent = actualRegion.getHost() + "api/lol/" + actualRegion.getPlatform() + "/v1.3/game/by-summoner/" + summonerId + "/recent?" + API_KEY;
                CompletionStage<JsonNode> recent = wsClient.url(urlRecent).get().thenApply(WSResponse::asJson);
                recent.handle((jsonNode2, throwable2) -> {
                    RecentGamesDTO recentGamesDTO = Json.fromJson(jsonNode2, RecentGamesDTO.class);
                    for (GameDTO gameDTO : recentGamesDTO.games) {
                        ChampionDTO topchampion = null;
                        for (int j = 0; j < topchampions.size() && topchampion == null; j++) {
                            if (topchampions.get(j).championId == gameDTO.championId) {
                                topchampion = topchampions.get(j);
                                BuildDTO buildDTO = new BuildDTO();
                                buildDTO.item0 = gameDTO.stats.item0;
                                buildDTO.item1 = gameDTO.stats.item1;
                                buildDTO.item2 = gameDTO.stats.item2;
                                buildDTO.item3 = gameDTO.stats.item3;
                                buildDTO.item4 = gameDTO.stats.item4;
                                buildDTO.item5 = gameDTO.stats.item5;
                                buildDTO.item6 = gameDTO.stats.item6;
                                topchampion.buildDTO = buildDTO;
                            }
                        }
                    }
                    return "";
                });
                return "";
            });
            return "";
        });
        summonerDTO.champions = topchampions;
        return ok(home.render());
=======
        return ok();
    }


    public Result get(Long championId, String region) {
        List<Player> players = playerDAO.getAll();
        for (Player player : players) {
            for (Topchampions topchampion : player.getTopchampionsList()) {

            }
        }
        return ok(index.render(""));
>>>>>>> origin/master
    }
}

