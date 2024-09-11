package football.StatsManagement.model.data;

import football.StatsManagement.model.domain.json.PlayerGameStatForJson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class PlayerGameStat {
  private final int id;
  private int gameId;
  // この時点でのクラブと背番号は確定させる
  private int playerId;
  private int clubId;
  private int number;
  // 試合成績
  private boolean starter;
  private int goals;
  private int assists;
  private int minutes;
  private int yellowCards;
  private int redCards;

  // @Insert用
  public PlayerGameStat(PlayerGameStatForJson playerGameStatForJson) {
    this.id = 0;
    this.gameId = 0; // この時点では不明
    this.playerId = playerGameStatForJson.getPlayerId();
    this.clubId = 0; // この時点では不明
    this.number = 0; // この時点では不明
    this.starter = playerGameStatForJson.isStarter();
    this.goals = playerGameStatForJson.getGoals();
    this.assists = playerGameStatForJson.getAssists();
    this.minutes = playerGameStatForJson.getMinutes();
    this.yellowCards = playerGameStatForJson.getYellowCards();
    this.redCards = playerGameStatForJson.getRedCards();
  }

  public void setGameInfo(int gameId, int clubId, int number) {
    this.gameId = gameId;
    this.clubId = clubId;
    this.number = number;
  }
}
