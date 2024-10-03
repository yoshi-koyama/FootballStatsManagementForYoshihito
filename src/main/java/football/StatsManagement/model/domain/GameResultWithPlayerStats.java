package football.StatsManagement.model.domain;

import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.data.PlayerGameStat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameResultWithPlayerStats {
  private GameResult gameResult;
  private List<PlayerGameStat> homePlayerGameStats;
  private List<PlayerGameStat> awayPlayerGameStats;
}
