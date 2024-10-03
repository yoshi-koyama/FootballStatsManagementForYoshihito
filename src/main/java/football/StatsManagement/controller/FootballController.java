package football.StatsManagement.controller;

import football.StatsManagement.exception.ResourceNotFoundException;
import football.StatsManagement.model.domain.json.GameResultWithPlayerStatsForJson;
import football.StatsManagement.service.FootballService;
import football.StatsManagement.exception.FootballException;
import football.StatsManagement.model.data.Club;
import football.StatsManagement.model.data.Country;
import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.data.League;
import football.StatsManagement.model.data.Player;
import football.StatsManagement.model.data.PlayerGameStat;
import football.StatsManagement.model.data.Season;
import football.StatsManagement.model.domain.PlayerSeasonStat;
import football.StatsManagement.model.domain.json.ClubForJson;
import football.StatsManagement.model.domain.json.GameResultForJson;
import football.StatsManagement.model.domain.GameResultWithPlayerStats;
import football.StatsManagement.model.domain.json.PlayerForJson;
import football.StatsManagement.model.domain.json.LeagueForJson;
import football.StatsManagement.model.domain.json.PlayerGameStatForJson;
import football.StatsManagement.model.domain.Standing;
import football.StatsManagement.model.domain.json.SeasonForJson;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class FootballController {
  private FootballService service;

  @Autowired
  public FootballController(FootballService service) {
    this.service = service;
  }

  /**
   * 現在シーズンの取得
   * @return 現在シーズン
   */
  @Operation(summary = "現在シーズンの取得", description = "現在のシーズンを取得します")
  @GetMapping("/seasons/current")
  public Season getCurrentSeason() throws ResourceNotFoundException {
    return service.getCurrentSeason();
  }

  /**
   * 国IDに紐づく国の取得
   * @param countryId
   * @return 国
   */
  @Operation(summary = "国の取得", description = "国IDに紐づく国を取得します")
  @GetMapping("/countries/{countryId}")
  public Country getCountry(@PathVariable @Positive int countryId) throws ResourceNotFoundException {
    return service.getCountry(countryId);
  }

  /**
   * リーグIDに紐づくリーグの取得
   * @param leagueId
   * @return リーグ
   */
  @Operation(summary = "リーグの取得", description = "リーグIDに紐づくリーグを取得します")
  @GetMapping("/leagues/{leagueId}")
  public League getLeague(@PathVariable @Positive int leagueId) throws ResourceNotFoundException {
    return service.getLeague(leagueId);
  }

  /**
   * クラブIDに紐づくクラブの取得
   * @param clubId
   * @return クラブ
   */
  @Operation(summary = "クラブの取得", description = "クラブIDに紐づくクラブを取得します")
  @GetMapping("/clubs/{clubId}")
  public Club getClub(@PathVariable @Positive int clubId) throws ResourceNotFoundException {
    return service.getClub(clubId);
  }

  /**
   * 選手IDに紐づく選手の取得
   * @param playerId
   * @return 選手
   */
  @Operation(summary = "選手の取得", description = "選手IDに紐づく選手を取得します")
  @GetMapping("/players/{playerId}")
  public Player getPlayer(@PathVariable @Positive int playerId) throws ResourceNotFoundException {
    return service.getPlayer(playerId);
  }

  /**
   * 国一覧の取得
   * @return 国一覧
   */
  @Operation(summary = "国一覧の取得", description = "登録されている国の一覧を取得します")
  @GetMapping("/countries")
  public List<Country> getCountries() {
    return service.getCountries();
  }

  /**
   * 国IDに紐づくリーグ一覧の取得
   * @param countryId
   * @return リーグ一覧
   */
  @Operation(summary = "リーグ一覧の取得", description = "国IDに紐づくリーグの一覧を取得します")
  @GetMapping("/countries/{countryId}/leagues")
  public List<League> getLeagues(@PathVariable @Positive int countryId) {
    return service.getLeaguesByCountry(countryId);
  }

  /**
   * リーグIDに紐づくクラブ一覧の取得
   * @param leagueId
   * @return クラブ一覧
   */
  @Operation(summary = "クラブ一覧の取得", description = "リーグIDに紐づくクラブの一覧を取得します")
  @GetMapping("/leagues/{leagueId}/clubs")
  public List<Club> getClubs(@PathVariable @Positive int leagueId) {
    return service.getClubsByLeague(leagueId);
  }

  /**
   * リーグIDとシーズンIDに紐づく順位表の取得
   * @param leagueId
   * @param seasonId
   * @return 順位表
   */
  @Operation(summary = "順位表の取得", description = "リーグIDとシーズンIDに紐づく順位表を取得します")
  @GetMapping("/leagues/{leagueId}/standings/{seasonId}")
  public Standing getStanding(@PathVariable @Positive int leagueId, @PathVariable @Min(100000) int seasonId) throws ResourceNotFoundException {
    return Standing.initialStanding(leagueId, seasonId, service);
  }


  /**
   * クラブIDに紐づく選手一覧の取得
   * @param clubId
   * @return 選手一覧
   */
  @Operation(summary = "選手一覧の取得", description = "クラブIDに紐づく選手の一覧を取得します")
  @GetMapping("/clubs/{clubId}/players")
  public List<Player> getPlayers(@PathVariable @Positive int clubId) {
    return service.getPlayersByClub(clubId);
  }

  /**
   * 選手IDとシーズンIDに紐づく選手成績の取得
   * @param playerId
   * @param seasonId
   * @return 選手の試合成績リスト
   */
  @Operation(summary = "選手試合成績の取得", description = "選手IDとシーズンIDに紐づく選手成績を取得します")
  @GetMapping("/players/{playerId}/player-game-stats/{seasonId}")
  public List<PlayerGameStat> getPlayerGameStatsBySeason(@PathVariable @Positive int playerId, @PathVariable @Min(100000) int seasonId)
      throws ResourceNotFoundException {
    return service.getPlayerGameStatsByPlayerAndSeason(playerId, seasonId);
  }

  /**
   * クラブIDとシーズンIDに紐づく選手成績の取得
   * @param clubId
   * @param seasonId
   * @return 選手のシーズン成績リスト
   */
  @Operation(summary = "クラブ所属選手シーズン成績の取得", description = "クラブIDとシーズンIDに紐づく選手シーズン成績を取得します")
  @GetMapping("/clubs/{clubId}/players-season-stats/{seasonId}")
  public List<PlayerSeasonStat> getPlayerSeasonStatsByClubId(@PathVariable @Positive int clubId, @PathVariable @Min(100000) int seasonId)
      throws ResourceNotFoundException {
    return service.getPlayerSeasonStatsByClubId(clubId, seasonId);
  }

  /**
   * 選手IDとシーズンIDに紐づく選手成績の取得
   * @param playerId
   * @param seasonId
   * @return 選手のシーズン成績
   */
  @Operation(summary = "選手シーズン成績の取得", description = "選手IDとシーズンIDに紐づく選手成績を取得します")
  @GetMapping("/players/{playerId}/player-season-stat/{seasonId}")
  public List<PlayerSeasonStat> getPlayerSeasonStat(@PathVariable @Positive int playerId, @PathVariable @Min(100000) int seasonId)
      throws ResourceNotFoundException {
    return service.getPlayerSeasonStatByPlayerId(playerId, seasonId);
  }

  /**
   * 選手IDに紐づく通算成績の取得
   * @param playerId
   * @return 選手のシーズン成績リスト
   */
  @GetMapping("/players/{playerId}/player-season-stats")
  public List<PlayerSeasonStat> getPlayerSeasonStatsByPlayerId(@PathVariable @Positive int playerId)
      throws ResourceNotFoundException {
    return service.getPlayerSeasonStatsByPlayerId(playerId);
  }


  /**
   * 試合IDに紐づく試合結果の取得（選手成績を含む）
   * @param gameId
   * @return 試合結果
   */
  @Operation(summary = "試合結果の取得", description = "試合IDに紐づく試合結果を取得します")
  @GetMapping("/game-results/{gameId}")
  public GameResult getGameResult(@PathVariable @Positive int gameId) throws ResourceNotFoundException {
    return service.getGameResult(gameId);
  }

  /**
   * 国の登録
   * @param name
   * @return 登録された国
   */
  @Operation(summary = "国の登録", description = "国を新規登録します")
  @PostMapping("/country")
  public ResponseEntity<Country> registerCountry(@RequestParam @NotBlank String name) {
    Country country = new Country(name);
    service.registerCountry(country);

    return ResponseEntity.ok().body(country);
  }

  /**
   * リーグの登録
   * @param leagueForJson
   * @return 登録されたリーグ
   */
  @Operation(summary = "リーグの登録", description = "リーグを新規登録します")
  @PostMapping("/league")
  public ResponseEntity<League> registerLeague(@RequestBody @Valid LeagueForJson leagueForJson) {
    League league = new League(leagueForJson);
    service.registerLeague(league);

    return ResponseEntity.ok().body(league);
  }

  /**
   * クラブの登録
   * @param clubForJson
   * @return 登録されたクラブ
   */
  @Operation(summary = "クラブの登録", description = "クラブを新規登録します")
  @PostMapping("/club")
  public ResponseEntity<Club> registerClub(@RequestBody @Valid ClubForJson clubForJson) {
    Club club = new Club(clubForJson);
    service.registerClub(club);

    return ResponseEntity.ok().body(club);
  }

  /**
   * 選手の登録
   * @param playerForJson
   * @return 登録された選手
   */
  @Operation(summary = "選手の登録", description = "選手を新規登録します")
  @PostMapping("/player")
  public ResponseEntity<Player> registerPlayer(@RequestBody @Valid PlayerForJson playerForJson) throws FootballException {
    Player player = new Player(playerForJson);
    service.registerPlayer(player);

    return ResponseEntity.ok().body(player);
  }

  /**
   * 試合結果の登録
   * @param gameResultWithPlayerStatsForJson
   * @return 登録された試合結果
   */
  @Operation(summary = "試合結果の登録", description = "試合結果を新規登録します")
  @PostMapping("/game-result")
  public ResponseEntity<GameResultWithPlayerStats> registerGameResult(
      @RequestBody @Valid GameResultWithPlayerStatsForJson gameResultWithPlayerStatsForJson)
      throws FootballException, ResourceNotFoundException {
    GameResult gameResult = new GameResult(gameResultWithPlayerStatsForJson.getGameResult());
    List<PlayerGameStat> homeClubStats = service.convertPlayerGameStatsForInsertToPlayerGameStats(gameResultWithPlayerStatsForJson.getHomeClubPlayerGameStats());
    List<PlayerGameStat> awayClubStats = service.convertPlayerGameStatsForInsertToPlayerGameStats(gameResultWithPlayerStatsForJson.getAwayClubPlayerGameStats());
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);

    service.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats);

    return ResponseEntity.ok().body(gameResultWithPlayerStats);
  }

  /**
   * シーズンの登録
   * @param seasonForJson
   * @return 登録されたシーズン
   */
  @Operation(summary = "シーズンの登録", description = "シーズンを新規登録します")
  @PostMapping("/season")
  public ResponseEntity<Season> registerSeason(@RequestBody @Valid SeasonForJson seasonForJson) throws FootballException {
    Season season = new Season(seasonForJson);
    service.registerSeason(season);

    return ResponseEntity.ok().body(season);
  }

  /**
   * 選手情報の更新
   * @param player
   * @return 更新された選手情報
   */
  @Operation(summary = "選手情報の更新", description = "選手情報を更新します")
  @PutMapping("/player")
  public ResponseEntity<Player> updatePlayer(@RequestBody @Valid Player player) throws FootballException, ResourceNotFoundException {
    service.updatePlayer(player);

    return ResponseEntity.ok().body(player);
  }


}
