package football.StatsManagement.data;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerSeasonStat {
  private Player player;
  private int games;
  private int starterGames;
  private int substituteGames;
  private int goals;
  private int assists;
  private int minutes;
  private int yellowCards;
  private int redCards;
}
