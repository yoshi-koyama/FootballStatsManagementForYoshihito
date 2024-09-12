package football.StatsManagement;

import football.StatsManagement.exception.FootballException;
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
import java.util.stream.Collectors;
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
  /**
   * Register a country
   * @param country
   */
  @Transactional
  public void registerCountry(Country country) {
    repository.insertCountry(country);
  }

  /**
   * Register a league
   * @param league
   */
  @Transactional
  public void registerLeague(League league) {
    repository.insertLeague(league);
  }

  /**
   * Register a club
   * @param club
   */
  @Transactional
  public void registerClub(Club club) {
    repository.insertClub(club);
  }

  /**
   * Register a player
   * @param player
   */
  @Transactional
  public void registerPlayer(Player player) {
    repository.insertPlayer(player);
  }

  /**
   * Register a player game stat
   * @param playerGameStats
   */
  @Transactional
  public void registerPlayerGameStat(PlayerGameStat playerGameStats) {
    repository.insertPlayerGameStat(playerGameStats);
  }

  /**
   * Register a game result
   * @param gameResult
   */
  @Transactional
  public void registerGameResult(GameResult gameResult) {
    repository.insertGameResult(gameResult);
  }

  /**
   * Register a season
   * @param season
   */
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
  /**
   * Get a country
   * @param id
   * @return a country
   */
  public Country getCountry(int id) {
    return repository.selectCountry(id);
  }

  /**
   * Get a league
   * @param id
   * @return a league
   */
  public League getLeague(int id) {
    return repository.selectLeague(id);
  }

  /**
   * Get a club
   * @param id
   * @return a club
   */
  public Club getClub(int id) {
    return repository.selectClub(id);
  }

  /**
   * Get a player
   * @param id
   * @return a player
   */
  public Player getPlayer(int id) {
    return repository.selectPlayer(id);
  }

  /**
   * Get a game result
   * @param id
   * @return a game result
   */
  public GameResult getGameResult(int id) {
    return repository.selectGameResult(id);
  }

  /**
   * Get a player game stat
   * @param id
   * @return a player game stat
   */
  public PlayerGameStat getPlayerGameStat(int id) {
    return repository.selectPlayerGameStat(id);
  }

  /**
   * Get player game stats by player
   * @param playerId
   * @return
   */
  public List<PlayerGameStat> getPlayerGameStatsByPlayer(int playerId) {
    return repository.selectPlayerGameStatsByPlayer(playerId);
  }

  /**
   * Get game results by league and season
   * @param seasonId
   * @param clubId
   * @return
   */
  public List<GameResult> getGameResultsByClubAndSeason(int seasonId, int clubId) {
    return repository.selectGameResultsByClubAndSeason(seasonId, clubId);
  }

  /**
   * Get players by club
   * @param clubId
   * @return players
   */
  public List<Player> getPlayersByClub(int clubId) {
    return repository.selectPlayersByClub(clubId);
  }

  /**
   * Get clubs by league
   * @param leagueId
   * @return clubs
   */
  public List<Club> getClubsByLeague(int leagueId) {
    return repository.selectClubsByLeague(leagueId);
  }

  /**
   * Get leagues by country
   * @param countryId
   * @return leagues
   */
  public List<League> getLeaguesByCountry(int countryId) {
    return repository.selectLeaguesByCountry(countryId);
  }

  /**
   * Get countries
   * @return countries
   */
  public List<Country> getCountries() {
    return repository.selectCountries();
  }

  /**
   * Get seasons
   * @return seasons
   */
  public List<Season> getSeasons() {
    return repository.selectSeasons();
  }

//  update

  /**
   * Update a player
   * @param player
   */
  @Transactional
  public void updatePlayer(Player player) {
    repository.updatePlayer(player);
  }


//  other
  /**
   * Get player season stats
   * @param clubId
   * @param seasonId
   * @return player season stats
   */
  public List<PlayerSeasonStat> getPlayerSeasonStatsByClubId(int clubId, int seasonId) {
    List<Player> players = repository.selectPlayersByClub(clubId);
    List<PlayerSeasonStat> playerSeasonStats = new ArrayList<>();
    for (Player player : players) {
      PlayerSeasonStat playerSeasonStat = getPlayerSeasonStatByPlayerId(player.getId(), seasonId);
      playerSeasonStats.add(playerSeasonStat);
    }
    return playerSeasonStats;
  }

  /**
   * Get player season stat by player ID
   * @param playerId
   * @param seasonId
   * @return player season stat
   */
  public PlayerSeasonStat getPlayerSeasonStatByPlayerId(int playerId, int seasonId) {
    // playerIdに紐づくPlayerGameStatを取得し、seasonIdに紐づくものだけを抽出
    Player player = repository.selectPlayer(playerId);
    List<PlayerGameStat> playerGameStats = repository.selectPlayerGameStatsByPlayer(playerId);
    List<GameResult> gameResultsInSeason = repository.selectGameResultsByClubAndSeason(seasonId, player.getClubId());
    List<PlayerGameStat> playerGameStatsInSeason = playerGameStats.stream()
        .filter(playerGameStat -> gameResultsInSeason.stream()
            .anyMatch(gameResult -> playerGameStat.getGameId() == gameResult.getId()))
        .collect(Collectors.toList());

    // PlayerSeasonStatを作成し、PlayerGameStatsから集計
    PlayerSeasonStat playerSeasonStat = new PlayerSeasonStat(player, seasonId, player.getClubId());
    aggregatePlayerSeasonStat(playerSeasonStat, playerGameStatsInSeason);

    return playerSeasonStat;
  }

  /**
   * Get player season stats by player ID
   * @param playerId
   * @return player season stats
   */
  public List<PlayerSeasonStat> getPlayerSeasonStatsByPlayerId(int playerId) {
    List<PlayerSeasonStat> playerSeasonStats = new ArrayList<>();
    List<Season> seasons = repository.selectSeasons();
    for (Season season : seasons) {
      PlayerSeasonStat playerSeasonStat = getPlayerSeasonStatByPlayerId(playerId, season.getId());
      if (playerSeasonStat != null) {
        playerSeasonStats.add(playerSeasonStat);
      }
    }
    return playerSeasonStats;
  }

  /**
   * Aggregate player season stat
   * @param playerSeasonStat
   * @param playerGameStats
   */
  private void aggregatePlayerSeasonStat(PlayerSeasonStat playerSeasonStat, List<PlayerGameStat> playerGameStats) {
    for (PlayerGameStat playerGameStat : playerGameStats) {
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
  }

  /**
   * Get player game stats except absent players
   * @param playerGameStats
   * @return player game stats except absent players
   */
  public List<PlayerGameStat> getPlayerGameStatsExceptAbsent(List<PlayerGameStat> playerGameStats) {
    List<PlayerGameStat> playerGameStatsExceptAbsent = new ArrayList<>();
    for (PlayerGameStat playerGameStat : playerGameStats) {
      if (playerGameStat.getMinutes() > 0) {
        playerGameStatsExceptAbsent.add(playerGameStat);
      }
    }
    return playerGameStatsExceptAbsent;
  }

  /**
   * Calculate score from player game stats
   * @param playerGameStats
   * @return score
   */
  public int getScoreByPlayerGameStats(List<PlayerGameStat> playerGameStats) {
    int score = 0;
    for (PlayerGameStat playerGameStat : playerGameStats) {
      score += playerGameStat.getGoals();
    }
    return score;
  }

  /**
   * Get winner club ID
   * @param homeScore
   * @param awayScore
   * @param homeClubId
   * @param awayClubId
   * @return winner club ID
   */
  public int getWinnerClubId(int homeScore, int awayScore, int homeClubId, int awayClubId) {
    if (homeScore > awayScore) {
      return homeClubId;
    } else if (homeScore < awayScore) {
      return awayClubId;
    } else {
      return 0;
    }
  }

  /**
   * Register game result and player game stats
   * @param gameResultWithPlayerStats
   */
  @Transactional
  public void registerGameResultAndPlayerGameStats(GameResultWithPlayerStats gameResultWithPlayerStats) throws FootballException {
    GameResult gameResult = gameResultWithPlayerStats.getGameResult();
    //    個人成績から出場なしの選手を除外
    List<PlayerGameStat> homeClubStats = gameResultWithPlayerStats.getHomeClubStats();
    List<PlayerGameStat> awayClubStats = gameResultWithPlayerStats.getAwayClubStats();

    homeClubStats = getPlayerGameStatsExceptAbsent(homeClubStats);
    awayClubStats = getPlayerGameStatsExceptAbsent(awayClubStats);

    // スタッツの整合性を確認
    confirmGameResultAndPlayerGameStats(gameResultWithPlayerStats, homeClubStats, awayClubStats);

    // 勝者を設定
    gameResult.setWinnerClubId(
        getWinnerClubId(gameResult.getHomeScore(), gameResult.getAwayScore(), gameResult.getHomeClubId(), gameResult.getAwayClubId()));

//    試合結果を登録
    registerGameResult(gameResult);

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

  /**
   * Confirm game result and player game stats
   * @param gameResultWithPlayerStats
   * @param homeClubStats
   * @param awayClubStats
   * @throws FootballException
   */
  private void confirmGameResultAndPlayerGameStats(GameResultWithPlayerStats gameResultWithPlayerStats, List<PlayerGameStat> homeClubStats, List<PlayerGameStat> awayClubStats) throws FootballException {
    // スコアが正しいか確認
    int homeScore = gameResultWithPlayerStats.getGameResult().getHomeScore();
    int awayScore = gameResultWithPlayerStats.getGameResult().getAwayScore();
    int homeScoreCalculated = getScoreByPlayerGameStats(homeClubStats);
    int awayScoreCalculated = getScoreByPlayerGameStats(awayClubStats);
    if (homeScore != homeScoreCalculated) {
      throw new FootballException("Home score is not correct");
    }
    if (awayScore != awayScoreCalculated) {
      throw new FootballException("Away score is not correct");
    }
    // starterの人数確認
    int homeStarterCount = (int) homeClubStats.stream().filter(PlayerGameStat::isStarter).count();
    int awayStarterCount = (int) awayClubStats.stream().filter(PlayerGameStat::isStarter).count();
    if (homeStarterCount != 11) {
      throw new FootballException("Home starter count is not correct");
    }
    if (awayStarterCount != 11) {
      throw new FootballException("Away starter count is not correct");
    }
    // 出場時間が合計990分か確認
    int homeMinutes = homeClubStats.stream().mapToInt(PlayerGameStat::getMinutes).sum();
    int awayMinutes = awayClubStats.stream().mapToInt(PlayerGameStat::getMinutes).sum();
    if (homeMinutes != 990) {
      throw new FootballException("Home minutes is not correct");
    }
    if (awayMinutes != 990) {
      throw new FootballException("Away minutes is not correct");
    }
  }

  public LocalDate convertStringToLocalDate(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate gameDate = LocalDate.parse(dateString, formatter);
    return gameDate;
  }

  /**
   * Convert player game stats for insert to player game stats
   * @param playerGameStatsForInsert
   * @return player game stats
   */
  public List<PlayerGameStat> convertPlayerGameStatsForInsertToPlayerGameStats(List<PlayerGameStatForJson> playerGameStatsForInsert) {
    List<PlayerGameStat> playerGameStats = new ArrayList<>();
    for (PlayerGameStatForJson playerGameStatForJson : playerGameStatsForInsert) {
      PlayerGameStat playerGameStat = new PlayerGameStat(playerGameStatForJson);
      playerGameStats.add(playerGameStat);
    }
    return playerGameStats;
  }

  public GameResultWithPlayerStats getGameResultWithPlayerStats(int gameId) {
    GameResult gameResult = getGameResult(gameId);
    List<PlayerGameStat> homeClubStats = getPlayerGameStatsByPlayer(gameResult.getHomeClubId()).stream()
        .filter(playerGameStat -> playerGameStat.getGameId() == gameId)
        .collect(Collectors.toList());
    List<PlayerGameStat> awayClubStats = getPlayerGameStatsByPlayer(gameResult.getAwayClubId()).stream()
        .filter(playerGameStat -> playerGameStat.getGameId() == gameId)
        .collect(Collectors.toList());
    return new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
  }
}
