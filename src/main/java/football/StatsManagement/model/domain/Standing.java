package football.StatsManagement.model.domain;

import football.StatsManagement.service.FootballService;
import football.StatsManagement.model.data.Club;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record Standing(int leagueId, int seasonId, List<ClubForStanding> clubs) {

  public static Standing initialStanding(int leagueId, int seasonId, FootballService service) {
    List<Club> clubs = service.getClubsByLeague(leagueId);
    // リーグによって異なる順位決定方法
    List<ClubForStanding> clubForStandings = rankedClubsForStanding(clubs, seasonId, leagueId, service);
    // 順位を設定
    for (int i = 0; i < clubForStandings.size(); i++) {
      clubForStandings.get(i).setPosition(i + 1);
    }
    return new Standing(leagueId, seasonId, clubForStandings);
  }

  private static List<ClubForStanding> rankedClubsForStanding(List<Club> clubs, int seasonId, int leagueId, FootballService service) {
    List<ClubForStanding> clubForStandings = clubs.stream()
        .map(club -> ClubForStanding.initialClubForStanding(seasonId, club, service))
        .toList();

    List<ClubForStanding> sortedClubForStandings = new ArrayList<>();
    // プリメーラ・ディビシオン
    if (leagueId == 7) {
      sortedClubForStandings = sortedClubForStandingsInPrimeraDivision(clubForStandings);
    }

    return sortedClubForStandings;
  }

  private static List<ClubForStanding> sortedClubForStandingsInPrimeraDivision(List<ClubForStanding> clubForStandings) {
    return clubForStandings.stream()
        .sorted((c1, c2) -> {
          // ①勝ち点
          if (c1.getPoints() != c2.getPoints()) {
            return c2.getPoints() - c1.getPoints();
          }
          // ②当該チーム間の勝ち点（試合数が2試合に満たない場合は無効）
          if (c1.getPointsAgainst(c2) != c2.getPointsAgainst(c1)
              && c1.getGamesAgainst(c2) >= 2) {
            return c2.getPointsAgainst(c1) - c1.getPointsAgainst(c2);
          }
          // ③当該チーム間の得失点差（試合数が2試合に満たない場合は無効）
          if (c1.getGoalDifferencesAgainst(c2) != c2.getGoalDifferencesAgainst(c1)
              && c1.getGamesAgainst(c2) >= 2) {
            return c2.getGoalDifferencesAgainst(c1) - c1.getGoalDifferencesAgainst(c2);
          }
          // ④全試合の得失点差
          if (c1.getGoalDifference() != c2.getGoalDifference()) {
            return c2.getGoalDifference() - c1.getGoalDifference();
          }
          // ⑤全試合の得点
          if (c1.getGoalsFor() != c2.getGoalsFor()) {
            return c2.getGoalsFor() - c1.getGoalsFor();
          }
          // これ以降は現時点では考慮しない
          return 0;
        })
        .collect(Collectors.toList());
  }

  private static List<ClubForStanding> sortedClubForStandingsInEnglishPremierLeague(List<ClubForStanding> clubForStandings) {
    // プレミアリーグの順位決定方法
    return clubForStandings.stream()
        .sorted((c1, c2) -> {
          // ①勝ち点
          if (c1.getPoints() != c2.getPoints()) {
            return c2.getPoints() - c1.getPoints();
          }
          // ②得失点差
          if (c1.getGoalDifference() != c2.getGoalDifference()) {
            return c2.getGoalDifference() - c1.getGoalDifference();
          }
          // ③得点
          if (c1.getGoalsFor() != c2.getGoalsFor()) {
            return c2.getGoalsFor() - c1.getGoalsFor();
          }
          // ④当該チーム間の勝ち点
          if (c1.getPointsAgainst(c2) != c2.getPointsAgainst(c1)) {
            return c2.getPointsAgainst(c1) - c1.getPointsAgainst(c2);
          }
          // ⑤当該チーム間のアウェーゴール
          if (c1.getAwayGoalsAgainst(c2) != c2.getAwayGoalsAgainst(c1)) {
            return c2.getAwayGoalsAgainst(c1) - c1.getAwayGoalsAgainst(c2);
          }
          // これ以降は現時点では考慮しない
          return 0;
        })
        .collect(Collectors.toList());
  }

}
