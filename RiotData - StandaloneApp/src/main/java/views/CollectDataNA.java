package views;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import dao.PlayerDAO;
import dao.RegionDAO;
import dao.TopChampionsDAO;
import dto.ChampionMasteryDTO;
import dto.ChampionStatsDTO;
import dto.GameDTO;
import dto.PlayerDTO;
import dto.RankedStatsDTO;
import dto.RecentGamesDTO;
import java.lang.reflect.Type;
import models.Builds;
import models.BuildsPK;
import models.Player;
import models.Region;
import models.Topchampions;
import models.TopchampionsPK;
import static views.CollectDataLAS.storeData;

public class CollectDataNA {

    public static final String API_KEY = "api_key=<API_KEY>";
    public static final String END_POINT = "https://na.api.pvp.net/";

    private static EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("RiotDataPU");

    private static PlayerDAO playerDAO;
    private static TopChampionsDAO topChampionsDAO;
    private static RegionDAO regionDAO;

    public static void main(String[] args) {
        playerDAO = new PlayerDAO(entityManagerFactory.createEntityManager());
        topChampionsDAO = new TopChampionsDAO(entityManagerFactory.createEntityManager());
        regionDAO = new RegionDAO(entityManagerFactory.createEntityManager());
        storeData("25932965", "NA1", "na");
    }

    public static void storeData(String summonerId, String regionId, String region) {
        try {
            try {
                Thread.sleep(1500);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Entro a " + summonerId);
            Gson gson = new Gson();
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String masteryUrl = END_POINT + "championmastery/location/" + regionId + "/player/" + summonerId
                    + "/topchampions?count=4&" + API_KEY;
            String rankedUrl = END_POINT + "api/lol/" + region + "/v1.3/stats/by-summoner/" + summonerId
                    + "/ranked?season=SEASON2016&" + API_KEY;
            String urlRecent = END_POINT + "api/lol/" + region + "/v1.3/game/by-summoner/" + summonerId + "/recent?"
                    + API_KEY;

            HttpGet requestMastery = new HttpGet(masteryUrl);
            HttpGet requestRanked = new HttpGet(rankedUrl);
            HttpGet requestRecent = new HttpGet(urlRecent);

            HttpResponse resultMastery = httpClient.execute(requestMastery);
            try {
                Thread.sleep(500);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            String stringJsonMastery = EntityUtils.toString(resultMastery.getEntity(), "UTF-8");
            System.out.println(stringJsonMastery);

            HttpResponse resultRanked = httpClient.execute(requestRanked);
            try {
                Thread.sleep(500);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            String stringJsonRanked = EntityUtils.toString(resultRanked.getEntity(), "UTF-8");
            System.out.println(stringJsonRanked);

            HttpResponse resultRecent = httpClient.execute(requestRecent);
            try {
                Thread.sleep(500);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            String stringJsonRecent = EntityUtils.toString(resultRecent.getEntity(), "UTF-8");
            System.out.println(stringJsonRecent);

            Player player = playerDAO.getById(Long.parseLong(summonerId));
            if (player != null) {
            } else {
                player = createPlayer(summonerId, regionId);
                RankedStatsDTO rankedStatsDTO = gson.fromJson(stringJsonRanked, RankedStatsDTO.class);
                Type listType = new TypeToken<List<ChampionMasteryDTO>>() {
                }.getType();
                List<ChampionMasteryDTO> arrayList = gson.fromJson(stringJsonMastery, listType);
                List<Topchampions> topchampions = new ArrayList<>();
                if (rankedStatsDTO.champions != null) {
                    for (ChampionMasteryDTO championMasteryDTO : arrayList) {
                        ChampionStatsDTO championStatsDTO = null;
                        for (int i = 0; i < rankedStatsDTO.champions.size() && championStatsDTO == null; i++) {
                            if (rankedStatsDTO.champions.get(i) != null && rankedStatsDTO.champions.get(i).id == championMasteryDTO.championId) {
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
                                        championStatsDTO.stats.totalDeathsPerSession,
                                        championStatsDTO.stats.totalSessionsPlayed, championStatsDTO.stats.totalGoldEarned,
                                        championStatsDTO.stats.totalChampionKills, championStatsDTO.stats.totalSessionsLost,
                                        championStatsDTO.stats.totalSessionsWon);
                            }
                            topchampions.add(topchampion);
                        }
                    }
                    RecentGamesDTO recentGamesDTO = gson.fromJson(stringJsonRecent, RecentGamesDTO.class);
                    if (recentGamesDTO != null && recentGamesDTO.games != null) {
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
                                buildsPK.setGameId((int) gameDTO.gameId);
                                Builds build = new Builds();
                                build.setBuildsPK(buildsPK);
                                build.setItem0(gameDTO.stats.item0);
                                build.setItem1(gameDTO.stats.item1);
                                build.setItem2(gameDTO.stats.item2);
                                build.setItem3(gameDTO.stats.item3);
                                build.setItem4(gameDTO.stats.item4);
                                build.setItem5(gameDTO.stats.item5);
                                build.setItem6(gameDTO.stats.item6);
                                topchampions1.getBuildsList().add(build);
                            }
                            player.setTopchampionsList(topchampions);
                            playerDAO.save(player);
                            for (GameDTO gameDTO1 : recentGamesDTO.games) {
                                for (PlayerDTO playerDTO : gameDTO1.fellowPlayers) {                                    storeData(playerDTO.summonerId + "", regionId, region);
                                   storeData(playerDTO.summonerId + "", regionId, region);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static Topchampions createTopChampion(TopchampionsPK topchampionsPK, int totalAssists,
            int totalDeathsPerSession, int totalSessionsPlayed, int totalGoldEarned, int totalChampionKills,
            int totalSessionsLost, int totalSessionsWon) {
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

    public static Player createPlayer(String summonerId, String regionId) {
        Player player = new Player();
        player.setSummonerId(Long.parseLong(summonerId));
        Region region = regionDAO.getById(regionId);
        player.setPlatformId(region);
        return player;
    }

}
