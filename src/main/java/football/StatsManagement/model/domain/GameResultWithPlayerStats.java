package football.StatsManagement.model.domain;

import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.data.PlayerGameStat;
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
