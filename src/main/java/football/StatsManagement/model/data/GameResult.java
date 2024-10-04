package football.StatsManagement.model.data;

import football.StatsManagement.model.domain.json.GameResultForJson;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class GameResult {
  private final int id;
  private int homeClubId;
  private int awayClubId;
  private int homeScore;
  private int awayScore;
  private int winnerClubId; // 0 if draw
  private int leagueId;
  private LocalDate gameDate;
  private int seasonId;

  // @Insert用
  public GameResult(GameResultForJson gameResultForJson) {
    this.id = 0;
    this.homeClubId = gameResultForJson.getHomeClubId();
    this.awayClubId = gameResultForJson.getAwayClubId();
    this.homeScore = gameResultForJson.getHomeScore();
    this.awayScore = gameResultForJson.getAwayScore();
    if (homeScore > awayScore) {
      this.winnerClubId = homeClubId;
    } else if (homeScore < awayScore) {
      this.winnerClubId = awayClubId;
    } else {
      this.winnerClubId = 0;
    }
    this.leagueId = gameResultForJson.getLeagueId();
    this.gameDate = gameResultForJson.getGameDate();
    this.seasonId = gameResultForJson.getSeasonId();
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
    GameResult gameResult = (GameResult) obj;
    return id == gameResult.id
        && homeClubId == gameResult.homeClubId
        && awayClubId == gameResult.awayClubId
        && homeScore == gameResult.homeScore
        && awayScore == gameResult.awayScore
        && winnerClubId == gameResult.winnerClubId
        && leagueId == gameResult.leagueId
        && Objects.equals(gameDate, gameResult.gameDate)
        && seasonId == gameResult.seasonId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, homeClubId, awayClubId, homeScore, awayScore, winnerClubId, leagueId, gameDate, seasonId);
  }
}
