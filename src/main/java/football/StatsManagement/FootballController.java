package football.StatsManagement;

import football.StatsManagement.exception.FootballException;
import football.StatsManagement.model.data.Club;
import football.StatsManagement.model.data.Country;
import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.data.League;
import football.StatsManagement.model.data.Player;
import football.StatsManagement.model.data.PlayerGameStat;
import football.StatsManagement.model.data.Season;
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
   * 国IDに紐づく国の取得
   * @param countryId
   * @return 国
   */
  @Operation(summary = "国の取得", description = "国IDに紐づく国を取得します")
  @GetMapping("/countries/{countryId}")
  public Country getCountry(@PathVariable @Positive int countryId) {
    return service.getCountry(countryId);
  }

  /**
   * リーグIDに紐づくリーグの取得
   * @param leagueId
   * @return リーグ
   */
  @Operation(summary = "リーグの取得", description = "リーグIDに紐づくリーグを取得します")
  @GetMapping("/leagues/{leagueId}")
  public League getLeague(@PathVariable @Positive int leagueId) {
    return service.getLeague(leagueId);
  }

  /**
   * クラブIDに紐づくクラブの取得
   * @param clubId
   * @return クラブ
   */
  @Operation(summary = "クラブの取得", description = "クラブIDに紐づくクラブを取得します")
  @GetMapping("/clubs/{clubId}")
  public Club getClub(@PathVariable @Positive int clubId) {
    return service.getClub(clubId);
  }

  /**
   * 選手IDに紐づく選手の取得
   * @param playerId
   * @return 選手
   */
  @Operation(summary = "選手の取得", description = "選手IDに紐づく選手を取得します")
  @GetMapping("/players/{playerId}")
  public Player getPlayer(@PathVariable @Positive int playerId) {
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
  @GetMapping("/countries/{countryId}/leagues/{leagueId}/clubs")
  public List<Club> getClubs(@PathVariable @Positive int countryId, @PathVariable @Positive int leagueId) {
    return service.getClubsByLeague(leagueId);
  }

  /**
   * リーグIDとシーズンIDに紐づく順位表の取得
   * @param leagueId
   * @param seasonId
   * @return 順位表
   */
  // TODO: seasonIdの取得方法を検討（@RequestParamで取得するか、@PathVariableで取得するか）
  @Operation(summary = "順位表の取得", description = "リーグIDとシーズンIDに紐づく順位表を取得します")
  @GetMapping("leagues/{leagueId}/standings/{seasonId}")
  public Standing getStanding(@PathVariable @Positive int leagueId, @PathVariable @Positive int seasonId) {
    return Standing.initialStanding(leagueId, seasonId, service);
  }

  /**
   * クラブIDに紐づく選手一覧の取得
   * @param clubId
   * @return 選手一覧
   */
  @Operation(summary = "選手一覧の取得", description = "クラブIDに紐づく選手の一覧を取得します")
  @GetMapping("/countries/{countryId}/leagues/{leagueId}/clubs/{clubId}/players")
  public List<Player> getPlayers(@PathVariable @Positive int countryId, @PathVariable @Positive int leagueId, @PathVariable @Positive int clubId) {
    return service.getPlayersByClub(clubId);
  }

  /**
   * 選手IDに紐づく選手情報の取得
   * @param playerId
   * @return 選手情報
   */
  @Operation(summary = "選手情報の取得", description = "選手IDに紐づく選手情報を取得します")
  @GetMapping("/countries/{countryId}/leagues/{leagueId}/clubs/{clubId}/players/{playerId}")
  public Player getPlayer(@PathVariable @Positive int countryId, @PathVariable @Positive int leagueId, @PathVariable @Positive int clubId, @PathVariable @Positive int playerId) {
    return service.getPlayer(playerId);
  }

  /**
   * 試合IDに紐づく試合結果の取得（選手成績を含む）
   * @param gameId
   * @return 試合結果
   */
  @Operation(summary = "試合結果の取得", description = "試合IDに紐づく試合結果を取得します")
  @GetMapping("game-results/{gameId}")
  public GameResult getGameResult(@PathVariable @Positive int gameId) {
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
  public ResponseEntity<Player> registerPlayer(@RequestBody PlayerForJson playerForJson) {
    Player player = new Player(playerForJson);
    service.registerPlayer(player);

    return ResponseEntity.ok().body(player);
  }

  /**
   * 試合結果の登録
   * @param gameResultForJson
   * @param homeClubStatsForInsert
   * @param awayClubStatsForInsert
   * @return 登録された試合結果
   */
  @Operation(summary = "試合結果の登録", description = "試合結果を新規登録します")
  @PostMapping("/game-result")
  public ResponseEntity<GameResultWithPlayerStats> registerGameResult(
      @RequestBody @Valid GameResultForJson gameResultForJson,
      @RequestBody @Valid List<PlayerGameStatForJson> homeClubStatsForInsert,
      @RequestBody @Valid List<PlayerGameStatForJson> awayClubStatsForInsert)
      throws FootballException {
    GameResult gameResult = new GameResult(gameResultForJson);
    List<PlayerGameStat> homeClubStats = service.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForInsert);
    List<PlayerGameStat> awayClubStats = service.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForInsert);
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
  public ResponseEntity<Season> registerSeason(@RequestBody @Valid SeasonForJson seasonForJson, BindingResult result) throws FootballException {
    if (result.hasErrors()) {
      throw new FootballException("シーズン名の形式が不正です。/n シーズン名は「yyyy-yy」の形式で入力してください。");
    }
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
  @PatchMapping("/player")
  public ResponseEntity<Player> updatePlayer(@RequestBody @Valid Player player) {
    service.updatePlayer(player);

    return ResponseEntity.ok().body(player);
  }


}
