package football.StatsManagement;

import football.StatsManagement.data.Club;
import football.StatsManagement.data.Country;
import football.StatsManagement.data.GameResult;
import football.StatsManagement.data.League;
import football.StatsManagement.data.Player;
import football.StatsManagement.data.PlayerGameStat;
import football.StatsManagement.data.PlayerSeasonStat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FootballService {
  private final FootballRepository repository;

  @Autowired
  public FootballService(FootballRepository repository) {
    this.repository = repository;
  }

//  register
  public void registerCountry(Country country) {
    repository.insertCountry(country);
  }

  public void registerLeague(League league) {
    repository.insertLeague(league);
  }

  public void registerClub(Club club) {
    repository.insertClub(club);
  }

  public void registerPlayer(Player player) {
    repository.insertPlayer(player);
  }

  public void registerPlayerGameStat(PlayerGameStat playerGameStats) {
    repository.insertPlayerGameStat(playerGameStats);
  }

  public void registerGameResult(GameResult gameResult) {
    repository.insertGameResult(gameResult);
  }

//  get
  public Country getCountry(int id) {
    return repository.selectCountry(id);
  }

  public League getLeague(int id) {
    return repository.selectLeague(id);
  }

  public Club getClub(int id) {
    return repository.selectClub(id);
  }

  public Player getPlayer(int id) {
    return repository.selectPlayer(id);
  }

  public PlayerGameStat getPlayerGameStat(int id) {
    return repository.selectPlayerGameStat(id);
  }

  public List<PlayerGameStat> getPlayerGameStatsByPlayer(int playerId) {
    return repository.selectPlayerGameStatsByPlayer(playerId);
  }

  public List<Player> getPlayersByClub(int clubId) {
    return repository.selectPlayersByClub(clubId);
  }

  public List<Club> getClubsByLeague(int leagueId) {
    return repository.selectClubsByLeague(leagueId);
  }

  public List<League> getLeaguesByCountry(int countryId) {
    return repository.selectLeaguesByCountry(countryId);
  }

  public List<Country> getCountries() {
    return repository.selectCountries();
  }

//  update
  public void updatePlayer(Player player) {
    repository.updatePlayer(player);
  }

  public void updatePlayerGameStat(PlayerGameStat playerGameStat) {
    repository.updatePlayerGameStat(playerGameStat);
  }

//  other
  public List<PlayerSeasonStat> playerSeasonStats(int clubId) {
    List<Player> players = repository.selectPlayersByClub(clubId);
    List<PlayerSeasonStat> playerSeasonStats = new ArrayList<>();
    for (Player player : players) {
      PlayerSeasonStat playerSeasonStat = new PlayerSeasonStat();
      playerSeasonStat.setPlayer(player);
      playerSeasonStat.setGames(repository.selectPlayerGameStatsByPlayer(player.getId()).size());
      playerSeasonStat.setStarterGames(0);
      playerSeasonStat.setSubstituteGames(0);
      playerSeasonStat.setGoals(0);
      playerSeasonStat.setAssists(0);
      playerSeasonStat.setMinutes(0);
      playerSeasonStat.setYellowCards(0);
      playerSeasonStat.setRedCards(0);
      for (PlayerGameStat playerGameStat : repository.selectPlayerGameStatsByPlayer(player.getId())) {
        playerSeasonStat.setGoals(playerSeasonStat.getGoals() + playerGameStat.getGoals());
        playerSeasonStat.setAssists(playerSeasonStat.getAssists() + playerGameStat.getAssists());
        playerSeasonStat.setMinutes(playerSeasonStat.getMinutes() + playerGameStat.getMinutes());
        playerSeasonStat.setYellowCards(playerSeasonStat.getYellowCards() + playerGameStat.getYellowCards());
        playerSeasonStat.setRedCards(playerSeasonStat.getRedCards() + playerGameStat.getRedCards());
        if (playerGameStat.isStarter()) {
          playerSeasonStat.setStarterGames(playerSeasonStat.getStarterGames() + 1);
        } else {
          playerSeasonStat.setSubstituteGames(playerSeasonStat.getSubstituteGames() + 1);
        }
      }
      playerSeasonStats.add(playerSeasonStat);
    }
    return playerSeasonStats;
  }

  public List<PlayerGameStat> convertClubIdToPlayerGameStats(int clubId) {
    List<Player> players = repository.selectPlayersByClub(clubId);
    List<PlayerGameStat> playerGameStats = new ArrayList<>();
    for (Player player : players) {
      PlayerGameStat stat = new PlayerGameStat();
      stat.setPlayerId(player.getId());
      stat.setPlayerName(player.getName());
      stat.setClubId(clubId);
      playerGameStats.add(stat);
    }
    return playerGameStats;
  }

  public List<PlayerGameStat> playerGameStatsExceptAbsent(List<PlayerGameStat> playerGameStats) {
    List<PlayerGameStat> playerGameStatsExceptAbsent = new ArrayList<>();
    for (PlayerGameStat playerGameStat : playerGameStats) {
      if (playerGameStat.getMinutes() > 0) {
        playerGameStatsExceptAbsent.add(playerGameStat);
      }
    }
    return playerGameStatsExceptAbsent;
  }

  public int calculateScore(List<PlayerGameStat> playerGameStats) {
    int score = 0;
    for (PlayerGameStat playerGameStat : playerGameStats) {
      score += playerGameStat.getGoals();
    }
    return score;
  }

  public int winnerClubId(int homeScore, int awayScore, int homeClubId, int awayClubId) {
    if (homeScore > awayScore) {
      return homeClubId;
    } else if (homeScore < awayScore) {
      return awayClubId;
    } else {
      return 0;
    }
  }

  @Transactional
  public void registerGameResultAndPlayerGameStats(GameResult gameResult) {
//    出場なしの選手を除外
    List<PlayerGameStat> homeClubStats = gameResult.getHomeClubStats();
    List<PlayerGameStat> awayClubStats = gameResult.getAwayClubStats();

    homeClubStats = playerGameStatsExceptAbsent(homeClubStats);
    awayClubStats = playerGameStatsExceptAbsent(awayClubStats);
    //    試合結果を編集
    int homeScore = calculateScore(homeClubStats);
    int awayScore = calculateScore(awayClubStats);
    gameResult.setHomeScore(homeScore);
    gameResult.setAwayScore(awayScore);
    gameResult.setWinnerClubId(winnerClubId(homeScore, awayScore, gameResult.getHomeClubId(), gameResult.getAwayClubId()));


//    試合結果を登録
    registerGameResult(gameResult);

//    個人成績を登録（登録前に日付を設定）
    for (PlayerGameStat playerGameStat : homeClubStats) {
      playerGameStat.setGameDate(gameResult.getGameDate());
      registerPlayerGameStat(playerGameStat);
    }
    for (PlayerGameStat playerGameStat : awayClubStats) {
      playerGameStat.setGameDate(gameResult.getGameDate());
      registerPlayerGameStat(playerGameStat);
    }
  }

  public LocalDate convertStringToLocalDate(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate gameDate = LocalDate.parse(dateString, formatter);
    return gameDate;
  }
}
