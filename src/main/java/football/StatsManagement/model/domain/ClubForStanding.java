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
    int wins = getWins(gameResults, club.getId());
    int draws = getDraws(gameResults);
    int losses = gamesPlayed - wins - draws;
    int points = wins * 3 + draws;
    int goalsFor = getGoalsFor(gameResults, club.getId());
    int goalsAgainst = getGoalsAgainst(gameResults, club.getId());
    int goalDifference = goalsFor - goalsAgainst;

    return new ClubForStanding(gameResults, club, gamesPlayed, wins, draws, losses, points, goalsFor, goalsAgainst, goalDifference);
  }

  // コンストラクタ用の値を取得するメソッド
  private static int getWins(List<GameResult> gameResults, int clubId) {
    int wins = 0;
    for (GameResult gameResult : gameResults) {
      wins += gameResult.getWinnerClubId() == clubId ? 1 : 0;
    }
    return wins;
  }

  private static int getDraws(List<GameResult> gameResults) {
    int draws = 0;
    for (GameResult gameResult : gameResults) {
      draws += gameResult.getWinnerClubId() == 0 ? 1 : 0;
    }
    return draws;
  }

  private static int getGoalsFor(List<GameResult> gameResults, int clubId) {
    int goalsFor = 0;
    for (GameResult gameResult : gameResults) {
      goalsFor += gameResult.getHomeClubId() == clubId ? gameResult.getHomeScore() : 0;
      goalsFor += gameResult.getAwayClubId() == clubId ? gameResult.getAwayScore() : 0;
    }
    return goalsFor;
  }

  private static int getGoalsAgainst(List<GameResult> gameResults, int clubId) {
    int goalsAgainst = 0;
    for (GameResult gameResult : gameResults) {
      goalsAgainst += gameResult.getHomeClubId() == clubId ? gameResult.getAwayScore() : 0;
      goalsAgainst += gameResult.getAwayClubId() == clubId ? gameResult.getHomeScore() : 0;
    }
    return goalsAgainst;
  }

  // 2クラブ間の成績比較のためのメソッド
  public int getPointsAgainst(ClubForStanding clubAgainst) {
    List<GameResult> gameResults = this.getGameResults().stream()
        .filter(gameResult -> gameResult.getHomeClubId() == clubAgainst.getClub().getId() || gameResult.getAwayClubId() == clubAgainst.getClub().getId())
        .toList();
    int pointsAgainst = 0;
    for (GameResult gameResult : gameResults) {
      if (gameResult.getWinnerClubId() == this.getClub().getId()) {
        pointsAgainst += 3;
      } else if (gameResult.getWinnerClubId() == 0) {
        pointsAgainst += 1;
      }
    }
    return pointsAgainst;
  }

  public int getGoalDifferencesAgainst(ClubForStanding clubAgainst) {
    int differencesAgainst = 0;
    for (GameResult gameResult : this.getGameResults()) {
      if (gameResult.getHomeClubId() == clubAgainst.getClub().getId()) {
        differencesAgainst += gameResult.getAwayScore() - gameResult.getHomeScore();
      } else if (gameResult.getAwayClubId() == clubAgainst.getClub().getId()) {
        differencesAgainst += gameResult.getHomeScore() - gameResult.getAwayScore();
      }
    }
    return differencesAgainst;
  }

  public int getAwayGoalsAgainst(ClubForStanding clubAgainst) {
    int awayGoalsAgainst = 0;
    for (GameResult gameResult : this.getGameResults()) {
      if (gameResult.getHomeClubId() == clubAgainst.getClub().getId()) {
        awayGoalsAgainst += gameResult.getAwayScore();
      } else if (gameResult.getAwayClubId() == clubAgainst.getClub().getId()) {
        awayGoalsAgainst += gameResult.getHomeScore();
      }
    }
    return awayGoalsAgainst;
  }

  public int getGamesAgainst(ClubForStanding clubAgainst) {
    return (int) this.getGameResults().stream()
        .filter(gameResult -> gameResult.getHomeClubId() == clubAgainst.getClub().getId() || gameResult.getAwayClubId() == clubAgainst.getClub().getId())
        .count();
  }
}
