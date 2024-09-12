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

  // player, seasonId, clubIdを引数にとり、残りは0で初期化するコンストラクタ
  public PlayerSeasonStat(Player player, int seasonId, int clubId) {
    this.player = player;
    this.seasonId = seasonId;
    this.clubId = clubId;
    this.games = 0;
    this.starterGames = 0;
    this.substituteGames = 0;
    this.goals = 0;
    this.assists = 0;
    this.minutes = 0;
    this.yellowCards = 0;
    this.redCards = 0;
  }
}
