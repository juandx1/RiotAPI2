package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import dao.PlayerDAO;
import dao.RegionDAO;
import dao.TopChampionsDAO;
import dto.*;
import models.*;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 */
@Singleton
public class AsyncController extends Controller {

    private final ActorSystem actorSystem;
    private final ExecutionContextExecutor exec;
    private WSClient wsClient;
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("riotPersistenceUnit");

    public static final String API_KEY = "api_key=c23d24ee-d074-4000-a7f0-4711741570c4";
    public static final String END_POINT = "https://lan.api.pvp.net/";

    private PlayerDAO playerDAO;
    private TopChampionsDAO topChampionsDAO;
    private RegionDAO regionDAO;

    @Inject
    public AsyncController(ActorSystem actorSystem, ExecutionContextExecutor exec, WSClient wsClient) {
        this.actorSystem = actorSystem;
        this.exec = exec;
        this.wsClient = wsClient;
        playerDAO = new PlayerDAO(entityManagerFactory.createEntityManager());
        topChampionsDAO = new TopChampionsDAO(entityManagerFactory.createEntityManager());
        regionDAO = new RegionDAO(entityManagerFactory.createEntityManager());
    }

    /**
     * An action that returns a plain text message after a delay
     * of 1 second.
     * <p>
     * The configuration in the <code>routes</code> file means that this method
     * will be called when the application receives a <code>GET</code> request with
     * a path of <code>/message</code>.
     */
    public CompletionStage<Result> message() {
        return getFutureMessage(1, TimeUnit.SECONDS).thenApplyAsync(Results::ok, exec);
    }

    private CompletionStage<String> getFutureMessage(long time, TimeUnit timeUnit) {
        CompletableFuture<String> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
                Duration.create(time, timeUnit),
                () -> future.complete("Hi!"),
                exec
        );
        return future;
    }

    public Result storeTopChampions(String summonerId, String regionId, String region) {
        Player player = playerDAO.getById(Long.parseLong(summonerId));
        if (player != null) {
            return ok();
        } else {
            player = createPlayer(summonerId, regionId);
            String masteryUrl = END_POINT + "championmastery/location/" + regionId + "/player/" + summonerId + "/topchampions?count=4&" + API_KEY;
            CompletionStage<JsonNode> masteryStats = wsClient.url(masteryUrl).get()
                    .thenApply(WSResponse::asJson);
            final Player finalPlayer1 = player;
            masteryStats.handle((jsonNode, throwable) -> {
                String rankedUrl = END_POINT + "api/lol/" + region + "/v1.3/stats/by-summoner/" + summonerId + "/ranked?season=SEASON2016&" + API_KEY;
                CompletionStage<JsonNode> rankedStats = wsClient.url(rankedUrl).get().thenApply(WSResponse::asJson);
                rankedStats.handleAsync((jsonNode1, throwable1) -> {
                    RankedStatsDTO rankedStatsDTO = Json.fromJson(jsonNode1, RankedStatsDTO.class);
                    Iterator<JsonNode> jsonNodeIterator = jsonNode.elements();
                    List<Topchampions> topchampions = new ArrayList<>();
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
                            TopchampionsPK topchampionsPK = new TopchampionsPK();
                            topchampionsPK.setChampionId(championStatsDTO.id);
                            topchampionsPK.setSummonerId(Long.parseLong(summonerId));
                            Topchampions topchampion = topChampionsDAO.getById(topchampionsPK);
                            if (topchampion == null) {
                                topchampion = createTopChampion(topchampionsPK, championStatsDTO.stats.totalAssists,
                                        championStatsDTO.stats.totalDeathsPerSession, championStatsDTO.stats.totalSessionsPlayed,
                                        championStatsDTO.stats.totalGoldEarned, championStatsDTO.stats.totalChampionKills,
                                        championStatsDTO.stats.totalSessionsLost, championStatsDTO.stats.totalSessionsWon);
                            }
                            topchampions.add(topchampion);
                        }
                    }
                    String urlRecent = END_POINT + "api/lol/" + region + "/v1.3/game/by-summoner/" + summonerId + "/recent?" + API_KEY;
                    CompletionStage<JsonNode> recent = wsClient.url(urlRecent).get().thenApply(WSResponse::asJson);
                    recent.handleAsync((jsonNode2, throwable2) -> {
                        RecentGamesDTO recentGamesDTO = Json.fromJson(jsonNode2, RecentGamesDTO.class);
                        for (GameDTO gameDTO : recentGamesDTO.games) {
                            Topchampions topchampions1 = null;
                            for (int j = 0; j < topchampions.size() && topchampions1 == null; j++) {
                                if (topchampions.get(j).getTopchampionsPK().getChampionId() == gameDTO.championId) {
                                    topchampions1 = topchampions.get(j);
                                }
                            }
                            if (topchampions1 != null) {
                                BuildsPK buildsPK = new BuildsPK();
                                buildsPK.setChampionId(gameDTO.championId);
                                buildsPK.setSummonerId(Long.parseLong(summonerId));
                                buildsPK.setMatchId(gameDTO.gameId);
                                Builds build = new Builds();
                                build.setBuildsPK(buildsPK);
                                build.setItem0(gameDTO.stats.item0);
                                build.setItem1(gameDTO.stats.item1);
                                build.setItem2(gameDTO.stats.item2);
                                build.setItem3(gameDTO.stats.item3);
                                build.setItem4(gameDTO.stats.item4);
                                build.setItem5(gameDTO.stats.item5);
                                build.setItem6(gameDTO.stats.item6);
                                topchampions1.getBuilds().add(build);
                            }
                        }
                        finalPlayer1.setTopchampionsList(topchampions);
                        playerDAO.save(finalPlayer1);
                        for (GameDTO gameDTO : recentGamesDTO.games) {
                            for (PlayerDTO playerDTO : gameDTO.fellowPlayers) {
                                storeTopChampions(playerDTO.summonerId+"", "LA1", "LAN");
                            }
                        }
                        return ok();
                    });
                    return ok();
                });
                return ok();
            });
        }
        return ok();
    }

    private Topchampions createTopChampion(TopchampionsPK topchampionsPK, int totalAssists, int totalDeathsPerSession, int totalSessionsPlayed, int totalGoldEarned, int totalChampionKills, int totalSessionsLost, int totalSessionsWon) {
        Topchampions topchampion = new Topchampions();
        topchampion.setTopchampionsPK(topchampionsPK);
        topchampion.setAssits(totalAssists);
        topchampion.setDeaths(totalDeathsPerSession);
        topchampion.setGames(totalSessionsPlayed);
        topchampion.setGold(totalGoldEarned);
        topchampion.setKills(totalChampionKills);
        topchampion.setLost(totalSessionsLost);
        topchampion.setDeaths(totalSessionsWon);
        return topchampion;
    }

    public Player createPlayer(String summonerId, String regionId) {
        Player player = new Player();
        player.setSummonerId(Long.parseLong(summonerId));
        Region region = regionDAO.getById(regionId);
        player.setPlatformId(region);
        return player;
    }

}
