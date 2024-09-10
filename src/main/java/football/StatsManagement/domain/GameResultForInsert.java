package football.StatsManagement.domain;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameResultForInsert {
  @NotNull
  private int homeClubId;
  @NotNull
  private int awayClubId;
  @NotNull
  private int homeScore;
  @NotNull
  private int awayScore;
  @NotNull
  private int winnerClubId; // 0 if draw
  @NotNull
  private int leagueId;
  @NotNull
  private LocalDate gameDate;
  @NotNull
  private int seasonId;
}
