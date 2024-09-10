package football.StatsManagement.data;

import football.StatsManagement.domain.GameResultForInsert;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
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
  public GameResult(GameResultForInsert gameResultForInsert) {
    this.id = 0;
    this.homeClubId = gameResultForInsert.getHomeClubId();
    this.awayClubId = gameResultForInsert.getAwayClubId();
    this.homeScore = gameResultForInsert.getHomeScore();
    this.awayScore = gameResultForInsert.getAwayScore();
    this.winnerClubId = gameResultForInsert.getWinnerClubId();
    this.leagueId = gameResultForInsert.getLeagueId();
    this.gameDate = gameResultForInsert.getGameDate();
    this.seasonId = gameResultForInsert.getSeasonId();
  }
}
