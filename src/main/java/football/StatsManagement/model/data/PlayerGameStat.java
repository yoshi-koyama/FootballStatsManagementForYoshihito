package football.StatsManagement.model.data;

import football.StatsManagement.model.domain.json.PlayerGameStatForJson;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class PlayerGameStat {
  private final int id;
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
  // テスト用Inset文の都合上順序は最後に変更
  private int gameId;

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

  // テスト用にequalsとhashCodeをオーバーライド
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    PlayerGameStat playerGameStat = (PlayerGameStat) obj;
    return id == playerGameStat.id &&
        gameId == playerGameStat.gameId &&
        playerId == playerGameStat.playerId &&
        clubId == playerGameStat.clubId &&
        number == playerGameStat.number &&
        starter == playerGameStat.starter &&
        goals == playerGameStat.goals &&
        assists == playerGameStat.assists &&
        minutes == playerGameStat.minutes &&
        yellowCards == playerGameStat.yellowCards &&
        redCards == playerGameStat.redCards;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gameId, playerId, clubId, number, starter, goals, assists, minutes, yellowCards, redCards);
  }
}
