package football.StatsManagement.domain;

import football.StatsManagement.data.GameResult;
import football.StatsManagement.data.PlayerGameStat;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameResultWithPlayerStats {
  private final GameResult gameResult;
  private final List<PlayerGameStat> homeClubStats;
  private final List<PlayerGameStat> awayClubStats;

}
