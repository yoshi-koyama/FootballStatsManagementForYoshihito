package football.StatsManagement.data;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResult {
  private int id;
  private int homeClubId;
  private int awayClubId;
  private int homeScore;
  private int awayScore;
  private int winnerClubId; // 0 if draw
  private int leagueId;
  private LocalDate gameDate;

  private List<PlayerGameStat> homeClubStats;
  private List<PlayerGameStat> awayClubStats;
}
