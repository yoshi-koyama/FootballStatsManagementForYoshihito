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

  @NotNull
  @Positive
  private int homeClubId;

  @NotNull
  @Positive
  private int awayClubId;

  @NotNull
  @PositiveOrZero
  private int homeScore;

  @NotNull
  @PositiveOrZero
  private int awayScore;

  @NotNull
  @PositiveOrZero
  private int winnerClubId; // 0 if draw

  @NotNull
  @Positive
  private int leagueId;

  @NotNull
  private LocalDate gameDate;

  @NotNull
  @Positive
  private int seasonId;
}
