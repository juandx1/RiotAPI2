package dto;

import java.util.List;

/**
 * Created by Juan Manuel on 6/05/2016.
 */
public class GameDTO {

    public int championId;

    public List<PlayerDTO> fellowPlayers;

    public boolean invalid;

    public long gameId;

    public int spell1;

    public int spell2;

    public RawStatsDTO stats;

    public String subType;

    public GameDTO() {
    }
}
