package controllers;

import dto.BuildDTO;
import dto.ChampionDTO;
import dto.ChampionMasteryDTO;
import dto.ChampionProbablityDTO;
import dto.ChampionStatsDTO;
import dto.GameDTO;
import dto.RankedStatsDTO;
import dto.RecentGamesDTO;
import dto.SummonerDTO;
import dto.TopChampionDTO;
import models.ChampionStatics;
import models.Player;
import models.Region;
import models.Topchampions;
import models.TopchampionsPK;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Query;

import play.db.jpa.JPA;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
@Singleton
public class HomeController extends Controller {

	public static final String API_KEY = "api_key=490854b3-a5c6-4884-8608-e32561c626eb";

	/**
	 * An action that renders an HTML page with a welcome message. The
	 * configuration in the <code>routes</code> file means that this method will
	 * be called when the application receives a <code>GET</code> request with a
	 * path of <code>/</code>.
	 */
	public static void index() {
		render();
	}

	public static String getPlayerData(String summonerName, String platformId) {
		try {
			Region region = Region.findById(platformId);
			String summonerUrl = "https://" + region.getHost() + "/api/lol/"
					+ region.getPlatform() + "/v1.4/summoner/by-name/"
					+ summonerName.replaceAll(" ", "") + "?" + API_KEY;
			HttpResponse resultSummoner = WS.url(summonerUrl).get();
			String jsonStringSummoner = resultSummoner.getString();
			
			String[] splitSummoner = jsonStringSummoner.split(":");
			String summonerId = splitSummoner[2].split(",")[0].replaceAll("\"",
					"");
			String masteryUrl = "https://" + region.getHost()
					+ "/championmastery/location/" + region.getPlatformId()
					+ "/player/" + summonerId + "/topchampions?count=4&"
					+ API_KEY;
			String rankedUrl = "https://" + region.getHost() + "/api/lol/"
					+ region.getPlatform() + "/v1.3/stats/by-summoner/"
					+ summonerId + "/ranked?season=SEASON2016&" + API_KEY;
			HttpResponse resultMastery = WS.url(masteryUrl).get();
			String jsonStringMastery = resultMastery.getString();
			HttpResponse resultRanked = WS.url(rankedUrl).get();
			String jsonStringRanked = resultRanked.getString();
			Gson gson = new Gson();
			SummonerDTO summonerDTO = new SummonerDTO();
			RankedStatsDTO rankedStatsDTO = gson.fromJson(jsonStringRanked,
					RankedStatsDTO.class);
			Type listType = new TypeToken<List<ChampionMasteryDTO>>() {
			}.getType();
			List<ChampionMasteryDTO> arrayList = gson.fromJson(
					jsonStringMastery, listType);
			List<ChampionDTO> topchampions = new ArrayList<ChampionDTO>();
			if (rankedStatsDTO.champions != null) {
				summonerDTO.summonerId = rankedStatsDTO.summonerId;
				for (ChampionMasteryDTO championMasteryDTO : arrayList) {
					ChampionStatsDTO championStatsDTO = null;
					for (int i = 0; i < rankedStatsDTO.champions.size()
							&& championStatsDTO == null; i++) {
						if (rankedStatsDTO.champions.get(i) != null
								&& rankedStatsDTO.champions.get(i).id == championMasteryDTO.championId) {
							championStatsDTO = rankedStatsDTO.champions.get(i);
						}
					}
					if (championStatsDTO != null) {
						ChampionDTO championDTO = new ChampionDTO();
						ChampionStatics championStatics = ChampionStatics
								.findById(championStatsDTO.id);
						championDTO.championName = championStatics
								.getChampionName();
						championDTO.championSplashUrl = championStatics
								.getSplashArtUrl();
						championDTO.championSquareUrl = championStatics
								.getSquareUrl();
						championDTO.rol = championStatics.getRol();
						championDTO.championId = championStatsDTO.id;
						championDTO.cs = championStatsDTO.stats.totalMinionKills;		
						championDTO.assits = championStatsDTO.stats.totalAssists;
						championDTO.deaths = championStatsDTO.stats.totalDeathsPerSession;
						championDTO.games = championStatsDTO.stats.totalSessionsPlayed;
						championDTO.gold = championStatsDTO.stats.totalGoldEarned;
						championDTO.kills = championStatsDTO.stats.totalChampionKills;
						championDTO.losses = championStatsDTO.stats.totalSessionsLost;
						championDTO.wins = championStatsDTO.stats.totalSessionsWon;
						topchampions.add(championDTO);
					}
				}
			}
			summonerDTO.champions = topchampions;
			return gson.toJson(summonerDTO).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "not-found";
		}
	}

	public static String getBestRegionView(int championId, String platformId) {
		try {
			Topchampions topchampions = getBestRegion(championId, platformId);
			TopChampionDTO topChampionDTO = new TopChampionDTO();
			topChampionDTO.assits = topchampions.getAssits();
			topChampionDTO.deaths = topchampions.getDeaths();
			topChampionDTO.games = topchampions.getGames();
			topChampionDTO.gold = topchampions.getGold();
			topChampionDTO.kills = topchampions.getKills();
			topChampionDTO.lost = topchampions.getLost();
			topChampionDTO.win = topchampions.getWin();
			Gson gson = new Gson();
			return gson.toJson(topChampionDTO).toString();
		} catch (Exception e) {
			return "Error";
		}
	}

	public static String getBestWorldView(int championId) {
		Topchampions topchampions = getBestWorld(championId);
		TopChampionDTO topChampionDTO = new TopChampionDTO();
		topChampionDTO.assits = topchampions.getAssits();
		topChampionDTO.deaths = topchampions.getDeaths();
		topChampionDTO.games = topchampions.getGames();
		topChampionDTO.gold = topchampions.getGold();
		topChampionDTO.kills = topchampions.getKills();
		topChampionDTO.lost = topchampions.getLost();
		topChampionDTO.win = topchampions.getWin();
		Gson gson = new Gson();
		return gson.toJson(topChampionDTO).toString();
	}

	public static List<ChampionDTO> getMostUsedWorld() {
		int[] mostUsed = get5MostUsedWorld();
		List<ChampionDTO> champions = new ArrayList<ChampionDTO>();
		for (int i = 0; i < mostUsed.length; i++) {
			ChampionStatics championStatics = ChampionStatics
					.findById(mostUsed[i]);
			ChampionDTO championDTO = new ChampionDTO();
			championDTO.championId = mostUsed[i];
			championDTO.championName = championStatics.getChampionName();
			championDTO.championSquareUrl = championStatics.getSquareUrl();
			champions.add(championDTO);
		}
		return champions;
	}

	private static Topchampions getBestWorld(int championId) {
		String sql = "select tp.* from Topchampions tp where tp.championId = :championId and ((tp.games-tp.lost)/tp.games)*1000+tp.kills+tp.assits*0.1-tp.deaths+(tp.gold/10000) = (SELECT MAX(((tp1.games-tp1.lost)/tp1.games)*1000+tp1.kills+tp1.assits*0.1-tp1.deaths+(tp1.gold/10000)) FROM Topchampions tp1 where tp1.championId = :championId)";
		Query query = JPA.em().createNativeQuery(sql, Topchampions.class);
		query.setParameter("championId", championId);
		return (Topchampions) query.getResultList().get(0);
	}

	private static Topchampions getBestRegion(int championId, String platformId) {
		String sql = "select tp.* from Topchampions tp, Player p, Region r where tp.championId = :championId and tp.summonerId = p.summonerId and r.platformId = p.platformId and r.platformId =:region and ((tp.games-tp.lost)/tp.games)*1000+tp.kills+tp.assits*0.1-tp.deaths+(tp.gold/10000) = (SELECT MAX(((tp1.games-tp1.lost)/tp1.games)*1000+tp1.kills+tp1.assits*0.1-tp1.deaths+(tp1.gold/10000)) FROM Topchampions tp1 where tp1.championId = :championId and tp.summonerId = p.summonerId and r.platformId = p.platformId and r.platformId =:region)";
		Query query = JPA.em().createNativeQuery(sql, Topchampions.class);
		query.setParameter("championId", championId);
		query.setParameter("region", platformId);
		return (Topchampions) query.getResultList().get(0);
	}

	private static int[] get5MostUsedWorld() {
		String sql = "SELECT championId, count(championId) as count from topchampions group by championId order by count desc limit 5";
		Query query = JPA.em().createNativeQuery(sql);
		List result = query.getResultList();
		int[] top5 = new int[5];
		for (int i = 0; i < result.size(); i++) {
			Object[] obj = (Object[]) result.get(i);
			top5[i] = (Integer) obj[0];
		}
		return top5;
	}

	public static String getChampionProbabilityRegionView() {
		List<ChampionDTO> champions = getMostUsedWorld();
		List<ChampionProbablityDTO> championsProbablity = new ArrayList<ChampionProbablityDTO>();
		String[] regions = { "BR", "EUNE", "EUW", "JP", "KR", "LAN", "LAS",
				"NA", "OCE", "RU", "TR" };
		for (ChampionDTO championDTO : champions) {
			ChampionProbablityDTO championProbablityDTO = new ChampionProbablityDTO();
			championProbablityDTO.championId = championDTO.championId;
			championProbablityDTO.championName = championDTO.championName;
			championProbablityDTO.championSplashUrl = championDTO.championSplashUrl;
			championProbablityDTO.championSquareUrl = championDTO.championSquareUrl;
			String allWorld = "SELECT COUNT(*) FROM TopChampions tp";
			String championWorld = "SELECT COUNT(*) FROM Topchampions tp where tp.championId=:championId group by tp.championId order by tp.championId";
			Query queryAllWorld = JPA.em().createNativeQuery(allWorld);
			Query queryChampionWorld = JPA.em()
					.createNativeQuery(championWorld);
			queryChampionWorld.setParameter("championId",
					championDTO.championId);
			BigInteger countAllWorld = (BigInteger) queryAllWorld
					.getResultList().get(0);
			List b=queryChampionWorld.getResultList();
			BigInteger countChampionWorld = (BigInteger) queryChampionWorld
					.getResultList().get(0);
			for (int i = 0; i < regions.length; i++) {
				String championRegion = "SELECT COUNT(tp.*) FROM Region r, Player p, Topchampions tp where r.platformId = p.platformId and p.summonerId = tp.summonerId and tp.championId=:championId and r.platform =:platform group by tp.championId order by tp.championId";
				String allRegion = "SELECT COUNT(*) FROM Region r, Player p, TopChampions tp where tp.summonerId = p.summonerId and p.platformId = r.platformId and r.platform =:platform";
				Query queryChampionRegion = JPA.em().createNativeQuery(
						championRegion);
				Query queryAllRegion = JPA.em().createNativeQuery(allRegion);
				queryAllRegion.setParameter("platform", regions[i]);
				queryChampionRegion.setParameter("championId",
						championDTO.championId);
				queryChampionRegion.setParameter("platform", regions[i]);
				BigInteger countRegion = (BigInteger) queryChampionRegion
						.getResultList().get(0);
				BigInteger countAllRegion = (BigInteger) queryAllRegion
						.getResultList().get(0);
				if (i == 0)
					championProbablityDTO.brProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 1)
					championProbablityDTO.euneProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 2)
					championProbablityDTO.euwProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 3)
					championProbablityDTO.jpProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 4)
					championProbablityDTO.krProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 5)
					championProbablityDTO.lanProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 6)
					championProbablityDTO.lasProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 7)
					championProbablityDTO.naProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 8)
					championProbablityDTO.oceProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 9)
					championProbablityDTO.ruProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;
				else if (i == 10)
					championProbablityDTO.trProbability = (countRegion
							.doubleValue() / countAllRegion.doubleValue())*100;

				championProbablityDTO.worldProbability = (countChampionWorld.doubleValue()/countAllWorld.doubleValue())*100	;
			}
			championsProbablity.add(championProbablityDTO);
		}
		Gson gson = new Gson();
		return gson.toJson(championsProbablity).toString();
	}
}
