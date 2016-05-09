package dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alejandro on 8/05/2016.
 */
public class SummonerDTO {

    public long summonerId;

    public List<ChampionMasteryDTO> champions = new ArrayList<>();
}
