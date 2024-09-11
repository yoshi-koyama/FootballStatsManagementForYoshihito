package football.StatsManagement;

import football.StatsManagement.exception_handler.FootballException;
import football.StatsManagement.model.data.Club;
import football.StatsManagement.model.data.Country;
import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.data.League;
import football.StatsManagement.model.data.Player;
import football.StatsManagement.model.data.PlayerGameStat;
import football.StatsManagement.model.data.Season;
import football.StatsManagement.model.domain.GameResultWithPlayerStats;
import football.StatsManagement.model.domain.json.PlayerGameStatForJson;
import football.StatsManagement.model.domain.PlayerSeasonStat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
  @Transactional
  public void registerCountry(Country country) {
    repository.insertCountry(country);
  }

  @Transactional
  public void registerLeague(League league) {
    repository.insertLeague(league);
  }

  @Transactional
  public void registerClub(Club club) {
    repository.insertClub(club);
  }

  @Transactional
  public void registerPlayer(Player player) {
    repository.insertPlayer(player);
  }

  @Transactional
  public void registerPlayerGameStat(PlayerGameStat playerGameStats) {
    repository.insertPlayerGameStat(playerGameStats);
  }

  @Transactional
  public void registerGameResult(GameResult gameResult) {
    repository.insertGameResult(gameResult);
  }

  @Transactional
  public void registerSeason(Season season) throws FootballException {
    // startDateがendDateより後になっていないか確認
    if (season.getStartDate().isAfter(season.getEndDate())) {
      throw new FootballException("Start date should be before end date");
    }
    // 既存のシーズンと重複しないか確認
    List<Season> seasons = repository.selectSeasons();
    for (Season s : seasons) {
      if (s.getName().equals(season.getName())) {
        throw new FootballException("Season name is already used");
      }
      // 既存のシーズンと期間が重複しないか確認
      if (season.getEndDate().isAfter(s.getStartDate()) || season.getStartDate().isBefore(s.getEndDate())) {
        throw new FootballException("Season period is already used");
      }
    }
    repository.insertSeason(season);
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

  public List<GameResult> getGameResultsByClubAndSeason(int seasonId, int clubId) {
    return repository.selectGameResultsByClubAndSeason(seasonId, clubId);
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

  public List<Season> getSeasons() {
    return repository.selectSeasons();
  }

//  update
  @Transactional
  public void updatePlayer(Player player) {
    repository.updatePlayer(player);
  }

  @Transactional
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
  public void registerGameResultAndPlayerGameStats(GameResultWithPlayerStats gameResultWithPlayerStats) throws FootballException {
    // GameResultのScoreとPlayerGameStatのScoreが一致しているか確認
    int homeScore = gameResultWithPlayerStats.getGameResult().getHomeScore();
    int awayScore = gameResultWithPlayerStats.getGameResult().getAwayScore();
    int homeScoreCalculated = calculateScore(gameResultWithPlayerStats.getHomeClubStats());
    int awayScoreCalculated = calculateScore(gameResultWithPlayerStats.getAwayClubStats());
    if (homeScore != homeScoreCalculated || awayScore != awayScoreCalculated) {
      throw new FootballException("Score is not correct");
    }

    // 勝者を設定
    GameResult gameResult = gameResultWithPlayerStats.getGameResult();
    gameResult.setWinnerClubId(winnerClubId(homeScore, awayScore, gameResult.getHomeClubId(), gameResult.getAwayClubId()));

//    試合結果を登録
    registerGameResult(gameResult);

//    出場なしの選手を除外
    List<PlayerGameStat> homeClubStats = gameResultWithPlayerStats.getHomeClubStats();
    List<PlayerGameStat> awayClubStats = gameResultWithPlayerStats.getAwayClubStats();

    homeClubStats = playerGameStatsExceptAbsent(homeClubStats);
    awayClubStats = playerGameStatsExceptAbsent(awayClubStats);


//    個人成績を登録（登録前にgameIdとclubIdとnumberを設定）
    for (PlayerGameStat playerGameStat : homeClubStats) {
      playerGameStat.setGameInfo(gameResult.getId(), gameResult.getHomeClubId(), repository.selectPlayer(playerGameStat.getPlayerId()).getNumber());
      registerPlayerGameStat(playerGameStat);
    }
    for (PlayerGameStat playerGameStat : awayClubStats) {
      playerGameStat.setGameInfo(gameResult.getId(), gameResult.getAwayClubId(), repository.selectPlayer(playerGameStat.getPlayerId()).getNumber());
      registerPlayerGameStat(playerGameStat);
    }
  }

  public LocalDate convertStringToLocalDate(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate gameDate = LocalDate.parse(dateString, formatter);
    return gameDate;
  }

  // List<PlayerGameStatForInsert>をList<PlayerGameStat>に変換
  public List<PlayerGameStat> convertPlayerGameStatsForInsertToPlayerGameStats(List<PlayerGameStatForJson> playerGameStatsForInsert) {
    List<PlayerGameStat> playerGameStats = new ArrayList<>();
    for (PlayerGameStatForJson playerGameStatForJson : playerGameStatsForInsert) {
      PlayerGameStat playerGameStat = new PlayerGameStat(playerGameStatForJson);
      playerGameStats.add(playerGameStat);
    }
    return playerGameStats;
  }
}
