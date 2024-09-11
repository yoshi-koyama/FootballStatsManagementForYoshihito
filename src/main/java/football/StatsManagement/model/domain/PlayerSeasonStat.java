package football.StatsManagement.model.domain;

import football.StatsManagement.model.data.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerSeasonStat {
  private Player player;

  private int seasonId;
  private int clubId;

  private int games;
  private int starterGames;
  private int substituteGames;
  private int goals;
  private int assists;
  private int minutes;
  private int yellowCards;
  private int redCards;
}
