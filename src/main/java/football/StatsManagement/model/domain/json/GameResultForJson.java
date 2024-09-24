package football.StatsManagement.model.domain.json;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameResultForJson {

  @Positive
  private int homeClubId;

  @Positive
  private int awayClubId;

  @PositiveOrZero
  private int homeScore;

  @PositiveOrZero
  private int awayScore;

  @PositiveOrZero
  private int winnerClubId; // 0 if draw

  @Positive
  private int leagueId;

  @NotNull
  private LocalDate gameDate;

  @Positive
  private int seasonId;
}
