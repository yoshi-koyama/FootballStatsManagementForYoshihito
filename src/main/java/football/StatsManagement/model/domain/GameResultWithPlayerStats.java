package football.StatsManagement.model.domain;

import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.data.PlayerGameStat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record GameResultWithPlayerStats(GameResult gameResult, List<PlayerGameStat> homeClubStats, List<PlayerGameStat> awayClubStats) {

}
