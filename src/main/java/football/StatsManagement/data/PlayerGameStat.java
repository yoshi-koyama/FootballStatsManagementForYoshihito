package football.StatsManagement.data;

import football.StatsManagement.domain.PlayerGameStatForInsert;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
  public PlayerGameStat(PlayerGameStatForInsert playerGameStatForInsert) {
    this.id = 0;
    this.gameId = 0; // この時点では不明
    this.playerId = playerGameStatForInsert.getPlayerId();
    this.clubId = 0; // この時点では不明
    this.number = 0; // この時点では不明
    this.starter = playerGameStatForInsert.isStarter();
    this.goals = playerGameStatForInsert.getGoals();
    this.assists = playerGameStatForInsert.getAssists();
    this.minutes = playerGameStatForInsert.getMinutes();
    this.yellowCards = playerGameStatForInsert.getYellowCards();
    this.redCards = playerGameStatForInsert.getRedCards();
  }

  public void setGameInfo(int gameId, int clubId, int number) {
    this.gameId = gameId;
    this.clubId = clubId;
    this.number = number;
  }
}
