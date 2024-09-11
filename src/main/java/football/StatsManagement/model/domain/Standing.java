package football.StatsManagement.model.domain;

import football.StatsManagement.FootballService;
import football.StatsManagement.model.data.Club;
import java.util.List;
import lombok.Getter;

@Getter
public class Standing {

  private int leagueId;
  private int seasonId;
  private List<ClubForStanding> clubs;

  public Standing(int leagueId, int seasonId, List<ClubForStanding> clubs) {
    this.leagueId = leagueId;
    this.seasonId = seasonId;
    this.clubs = clubs;
  }

  public static Standing initialStanding(int leagueId, int seasonId, FootballService service) {
    List<Club> clubs = service.getClubsByLeague(leagueId);
    List<ClubForStanding> clubForStandings = clubs.stream()
        .map(club -> ClubForStanding.initialClubForStanding(seasonId, club, service))
        // 順位を設定（勝ち点以降の決め方はリーグによって変更が必要）
        // TODO: リーグによって順位を決める方法を変更する
        .sorted((c1, c2) -> {
          if (c1.getPoints() != c2.getPoints()) {
            return c2.getPoints() - c1.getPoints();
          } else if (c1.getGoalDifference() != c2.getGoalDifference()) {
            return c2.getGoalDifference() - c1.getGoalDifference();
          } else {
            return c2.getGoalsFor() - c1.getGoalsFor();
          }
        })
        .toList();
    // 順位を設定
    for (int i = 0; i < clubForStandings.size(); i++) {
      clubForStandings.get(i).setPosition(i + 1);
    }
    return new Standing(leagueId, seasonId, clubForStandings);
  }
}
