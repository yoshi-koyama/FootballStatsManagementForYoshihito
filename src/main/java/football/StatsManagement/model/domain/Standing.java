package football.StatsManagement.model.domain;

import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.service.FootballService;
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

    // プリメーラ・ディビシオン
    if (leagueId == 7) {
      clubForStandings = sortedClubForStandingsInPrimeraDivision(clubForStandings);
    }

    return clubForStandings;
  }

  private static List<ClubForStanding> sortedClubForStandingsInPrimeraDivision(List<ClubForStanding> clubForStandings) {
    clubForStandings.stream()
        .sorted((c1, c2) -> {
          // ①勝ち点
          if (c1.getPoints() != c2.getPoints()) {
            return c2.getPoints() - c1.getPoints();
          }
          // ②当該チーム間の勝ち点
          int c1PointsAgainstC2 = getPointsAgainst(c1, c2);
          int c2PointsAgainstC1 = getPointsAgainst(c2, c1);
          if (c1PointsAgainstC2 != c2PointsAgainstC1) {
            return c2PointsAgainstC1 - c1PointsAgainstC2;
          }
          // ③当該チーム間の得失点差
          int c1GoalsAgainstC2 = getGoalsAgainst(c1, c2);
          int c2GoalsAgainstC1 = getGoalsAgainst(c2, c1);
          if (c1GoalsAgainstC2 != c2GoalsAgainstC1) {
            return c2GoalsAgainstC1 - c1GoalsAgainstC2;
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
        });
    return clubForStandings;
  }

  private static List<ClubForStanding> sortedClubForStandingsInEnglishPremierLeague(List<ClubForStanding> clubForStandings) {
    // プレミアリーグの順位決定方法
    clubForStandings.stream()
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
          int c1PointsAgainstC2 = getPointsAgainst(c1, c2);
          int c2PointsAgainstC1 = getPointsAgainst(c2, c1);
          if (c1PointsAgainstC2 != c2PointsAgainstC1) {
            return c2PointsAgainstC1 - c1PointsAgainstC2;
          }
          // ⑤当該チーム間のアウェーゴール
          int c1AwayGoalsAgainstC2 = getAwayGoalsAgainst(c1, c2);
          int c2AwayGoalsAgainstC1 = getAwayGoalsAgainst(c2, c1);
          if (c1AwayGoalsAgainstC2 != c2AwayGoalsAgainstC1) {
            return c2AwayGoalsAgainstC1 - c1AwayGoalsAgainstC2;
          }
          // これ以降は現時点では考慮しない
          return 0;
        });
    return clubForStandings;
  }

  private static int getPointsAgainst(ClubForStanding clubA, ClubForStanding clubB) {
    int clubAId = clubA.getClub().getId();
    int clubBId = clubB.getClub().getId();
    List<GameResult> gameResults = clubA.getGameResults().stream()
        .filter(gameResult -> gameResult.getHomeClubId() == clubBId || gameResult.getAwayClubId() == clubBId)
        .toList();
    // 試合数が2試合に満たない場合は無効
    if (gameResults.size() < 2) {
      return 0;
    }
    int pointsAgainst = 0;
    for (GameResult gameResult : gameResults) {
      if (gameResult.getWinnerClubId() == clubAId) {
        pointsAgainst += 3;
      } else if (gameResult.getWinnerClubId() == 0) {
        pointsAgainst += 1;
      }
    }
    return pointsAgainst;
  }

  private static int getGoalsAgainst(ClubForStanding clubA, ClubForStanding clubB) {
    int clubAId = clubA.getClub().getId();
    int clubBId = clubB.getClub().getId();
    List<GameResult> gameResults = clubA.getGameResults().stream()
        .filter(gameResult -> gameResult.getHomeClubId() == clubBId || gameResult.getAwayClubId() == clubBId)
        .toList();
    int goalsAgainst = 0;
    for (GameResult gameResult : gameResults) {
      goalsAgainst += gameResult.getHomeClubId() == clubAId ? gameResult.getAwayScore() : 0;
      goalsAgainst += gameResult.getAwayClubId() == clubAId ? gameResult.getHomeScore() : 0;
    }
    return goalsAgainst;
  }

  private static int getAwayGoalsAgainst(ClubForStanding clubA, ClubForStanding clubB) {
    int clubAId = clubA.getClub().getId();
    int clubBId = clubB.getClub().getId();
    List<GameResult> gameResults = clubA.getGameResults().stream()
        .filter(gameResult -> gameResult.getHomeClubId() == clubBId || gameResult.getAwayClubId() == clubBId)
        .toList();
    int awayGoalsAgainst = 0;
    for (GameResult gameResult : gameResults) {
      awayGoalsAgainst += gameResult.getAwayClubId() == clubAId ? gameResult.getAwayScore() : 0;
    }
    return awayGoalsAgainst;
  }


}
