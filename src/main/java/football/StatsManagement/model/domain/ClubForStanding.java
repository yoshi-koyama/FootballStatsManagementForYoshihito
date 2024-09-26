package football.StatsManagement.model.domain;

import football.StatsManagement.service.FootballService;
import football.StatsManagement.model.data.Club;
import football.StatsManagement.model.data.GameResult;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubForStanding {

  private final List<GameResult> gameResults;

  private final Club club;
  private final int gamesPlayed;
  private final int wins;
  private final int draws;
  private final int losses;
  private final int points;
  private final int goalsFor;
  private final int goalsAgainst;
  private final int goalDifference;

  private int position; // これのみsetterを持つ

  // 順位は後で設定するので、コンストラクタには含めない
  public ClubForStanding(List<GameResult> gameResults, Club club, int gamesPlayed, int wins, int draws, int losses, int points, int goalsFor, int goalsAgainst, int goalDifference) {
    this.gameResults = gameResults;
    this.club = club;
    this.gamesPlayed = gamesPlayed;
    this.wins = wins;
    this.draws = draws;
    this.losses = losses;
    this.points = points;
    this.goalsFor = goalsFor;
    this.goalsAgainst = goalsAgainst;
    this.goalDifference = goalDifference;
  }

  public static ClubForStanding initialClubForStanding (int seasonId, Club club, FootballService service) {
    List<GameResult> gameResults = service.getGameResultsByClubAndSeason(seasonId, club.getId());
    int gamesPlayed = gameResults.size();
    int wins = getWins(gameResults, club.getId(), service);
    int draws = getDraws(gameResults, club.getId());
    int losses = gamesPlayed - wins - draws;
    int points = wins * 3 + draws;
    int goalsFor = getGoalsFor(gameResults, club.getId(), service);
    int goalsAgainst = getGoalsAgainst(gameResults, club.getId(), service);
    int goalDifference = goalsFor - goalsAgainst;

    return new ClubForStanding(gameResults, club, gamesPlayed, wins, draws, losses, points, goalsFor, goalsAgainst, goalDifference);
  }

  private static int getWins(List<GameResult> gameResults, int clubId, FootballService service) {
    int wins = 0;
    for (GameResult gameResult : gameResults) {
      wins += gameResult.getWinnerClubId() == clubId ? 1 : 0;
    }
    return wins;
  }

  private static int getDraws(List<GameResult> gameResults, int clubId) {
    int draws = 0;
    for (GameResult gameResult : gameResults) {
      draws += gameResult.getWinnerClubId() == 0 ? 1 : 0;
    }
    return draws;
  }

  private static int getGoalsFor(List<GameResult> gameResults, int clubId, FootballService service) {
    int goalsFor = 0;
    for (GameResult gameResult : gameResults) {
      goalsFor += gameResult.getHomeClubId() == clubId ? gameResult.getHomeScore() : 0;
      goalsFor += gameResult.getAwayClubId() == clubId ? gameResult.getAwayScore() : 0;
    }
    return goalsFor;
  }

  private static int getGoalsAgainst(List<GameResult> gameResults, int clubId, FootballService service) {
    int goalsAgainst = 0;
    for (GameResult gameResult : gameResults) {
      goalsAgainst += gameResult.getHomeClubId() == clubId ? gameResult.getAwayScore() : 0;
      goalsAgainst += gameResult.getAwayClubId() == clubId ? gameResult.getHomeScore() : 0;
    }
    return goalsAgainst;
  }
}
