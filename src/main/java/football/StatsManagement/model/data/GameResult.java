package football.StatsManagement.model.data;

import football.StatsManagement.model.domain.json.GameResultForJson;
import java.time.LocalDate;
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
    this.winnerClubId = gameResultForJson.getWinnerClubId();
    this.leagueId = gameResultForJson.getLeagueId();
    this.gameDate = gameResultForJson.getGameDate();
    this.seasonId = gameResultForJson.getSeasonId();
  }
}
