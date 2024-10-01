package football.StatsManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import football.StatsManagement.exception.FootballException;
import football.StatsManagement.exception.ResourceNotFoundException;
import football.StatsManagement.model.data.Club;
import football.StatsManagement.model.data.Country;
import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.data.League;
import football.StatsManagement.model.data.Player;
import football.StatsManagement.model.data.PlayerGameStat;
import football.StatsManagement.model.data.Season;
import football.StatsManagement.model.domain.GameResultWithPlayerStats;
import football.StatsManagement.model.domain.json.ClubForJson;
import football.StatsManagement.model.domain.json.GameResultForJson;
import football.StatsManagement.model.domain.json.LeagueForJson;
import football.StatsManagement.model.domain.json.PlayerForJson;
import football.StatsManagement.model.domain.json.PlayerGameStatForJson;
import football.StatsManagement.model.domain.json.SeasonForJson;
import football.StatsManagement.repository.FootballRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FootballServiceTest {

  @Mock
  private FootballRepository repository;

  private FootballService sut;
  @BeforeEach
  void setUp() {
    this.sut = new FootballService(repository);
  }

  @Test
  @DisplayName("国が登録できる_リポジトリが適切に処理されること")
  void registerCountry() {
    Country country = new Country("sampleName");
    sut.registerCountry(country);
    verify(repository, times(1)).insertCountry(country);
  }

  @Test
  @DisplayName("リーグが登録できる_リポジトリが適切に処理されること")
  void registerLeague() {
    LeagueForJson leagueForJson = new LeagueForJson(1, "sampleName");
    League league = new League(leagueForJson);
    sut.registerLeague(league);
    verify(repository, times(1)).insertLeague(league);
  }

  @Test
  @DisplayName("クラブが登録できる_リポジトリが適切に処理されること")
  void registerClub() {
    ClubForJson clubForJson = new ClubForJson(1, "sampleName");
    Club club = new Club(clubForJson);
    sut.registerClub(club);
    verify(repository, times(1)).insertClub(club);
  }

  @Test
  @DisplayName("選手が登録できる_リポジトリが適切に処理されること")
  void registerPlayer() throws FootballException {
    PlayerForJson playerForJson = new PlayerForJson(1, "sampleName", 1);
    Player player = new Player(playerForJson);
    sut.registerPlayer(player);
    verify(repository, times(1)).insertPlayer(player);
  }

  @Test
  void registerPlayer_withDuplicatedNumber() {
    PlayerForJson playerForJson = new PlayerForJson(1, "sampleName", 1);
    Player player = new Player(playerForJson);
    // 既に同じ背番号の選手が登録されている状態を作る
    when(sut.getPlayersByClub(1)).thenReturn(List.of(
        new Player(1, 1, "sampleName", 1)
    ));
    // 例外が投げられることを確認し、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerPlayer(player));
    assertEquals("Player number is already used", thrown.getMessage());
  }

  @Test
  @DisplayName("選手の試合スタッツが登録できる_リポジトリが適切に処理されること")
  void registerPlayerGameStat() {
    PlayerGameStatForJson playerGameStatForJson = new PlayerGameStatForJson(1, true, 1, 1, 1, 1, 1);
    PlayerGameStat playerGameStat = new PlayerGameStat(playerGameStatForJson);
    sut.registerPlayerGameStat(playerGameStat);
    verify(repository, times(1)).insertPlayerGameStat(playerGameStat);
  }

  @Test
  @DisplayName("試合結果が登録できる_リポジトリが適切に処理されること")
  void registerGameResult() {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 1, 1, 1, 1, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    sut.registerGameResult(gameResult);
    verify(repository, times(1)).insertGameResult(gameResult);
  }

  @Test
  @DisplayName("シーズンが登録できる_メソッドおよびリポジトリが適切に処理されること")
  void registerSeason() throws FootballException {
    SeasonForJson seasonForJson = new SeasonForJson("2000-01", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30));
    Season season = new Season(seasonForJson);
    sut.registerSeason(season);
    verify(repository, times(1)).insertSeason(season);
  }

  @Test
  @DisplayName("シーズン登録で、開始日と終了日が逆転している場合に適切に例外処理されること")
  void registerSeason_withReverseDate() {
    SeasonForJson seasonForJson = new SeasonForJson("2000-01", LocalDate.of(2001, 7, 1), LocalDate.of(2000, 6, 30));
    Season season = new Season(seasonForJson);
    // 例外が投げられることを確認し、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerSeason(season));

    // 期待される例外メッセージを確認
    assertEquals("Start date should be before end date", thrown.getMessage());
  }

  @Test
  void registerSeason_withDuplicatedName() {
    SeasonForJson seasonForJson = new SeasonForJson("2000-01", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30));
    Season season = new Season(seasonForJson);
    // 既に同じ名前のシーズンが登録されている状態を作る
    when(sut.getSeasons()).thenReturn(List.of(
        new Season(1, "2000-01", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30), true)
    ));
    // 例外が投げられることを確認し、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerSeason(season));
    assertEquals("Season name is already used", thrown.getMessage());
  }

  @Test
  @DisplayName("シーズン登録で、既に登録されているものと期間が重複している場合（開始部分）に適切に例外処理されること")
  void registerSeason_withDuplicatedDateAtStart() {
    SeasonForJson seasonForJson = new SeasonForJson("2000-01", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30));
    Season season = new Season(seasonForJson);
    // 既に期間が重複したが登録されている状態を作る（1999/7/1～2000/7/31）
    when(sut.getSeasons()).thenReturn(List.of(
        new Season(1, "1999-00", LocalDate.of(1999, 7, 1), LocalDate.of(2000, 7, 31), true)
    ));
    // 例外が投げられることを確認し、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerSeason(season));

    // 期待される例外メッセージを確認
    assertEquals("Season period is already used", thrown.getMessage());
  }

  @Test
  @DisplayName("シーズン登録で、既に登録されているものと期間が重複している場合（終了部分）に適切に例外処理されること")
  void registerSeason_withDuplicatedDateAtEnd() {
    SeasonForJson seasonForJson = new SeasonForJson("2000-01", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30));
    Season season = new Season(seasonForJson);
    // 既に期間が重複したが登録されている状態を作る（2001/6/1～2002/6/30）
    when(sut.getSeasons()).thenReturn(List.of(
        new Season(1, "2001-02", LocalDate.of(2001, 6, 1), LocalDate.of(2002, 6, 30), true)
    ));
    // 例外が投げられることを確認し、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerSeason(season));

    // 期待される例外メッセージを確認
    assertEquals("Season period is already used", thrown.getMessage());
  }

  @Test
  @DisplayName("IDによる国の検索_リポジトリが適切に処理されること")
  void getCountry() throws ResourceNotFoundException {
    Country country = new Country(1, "sampleName");
    when(repository.selectCountry(1)).thenReturn(Optional.of(country));
    Country actual = sut.getCountry(1);
    verify(repository, times(1)).selectCountry(1);
  }

  @Test
  @DisplayName("IDによる国の検索_IDが存在しない場合に適切に例外処理されること")
  void getCountry_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectCountry(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getCountry(1));
  }

  @Test
  @DisplayName("IDによるリーグの検索_リポジトリが適切に処理されること")
  void getLeague() throws ResourceNotFoundException {
    League league = new League(1, 1, "sampleName");
    when(repository.selectLeague(1)).thenReturn(Optional.of(league));
    League actual = sut.getLeague(1);
    verify(repository, times(1)).selectLeague(1);
  }

  @Test
  @DisplayName("IDによるリーグの検索_IDが存在しない場合に適切に例外処理されること")
  void getLeague_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectLeague(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getLeague(1));
  }

  @Test
  @DisplayName("IDによるクラブの検索_リポジトリが適切に処理されること")
  void getClub() throws ResourceNotFoundException {
    Club club = new Club(1, 1, "sampleName");
    when(repository.selectClub(1)).thenReturn(Optional.of(club));
    Club actual = sut.getClub(1);
    verify(repository, times(1)).selectClub(1);
  }

  @Test
  @DisplayName("IDによるクラブの検索_IDが存在しない場合に適切に例外処理されること")
  void getClub_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectClub(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getClub(1));
  }

  @Test
  @DisplayName("IDによる選手の検索_リポジトリが適切に処理されること")
  void getPlayer() throws ResourceNotFoundException {
    Player player = new Player(1, 1, "sampleName", 1);
    when(repository.selectPlayer(1)).thenReturn(Optional.of(player));
    Player actual = sut.getPlayer(1);
    verify(repository, times(1)).selectPlayer(1);
  }

  @Test
  @DisplayName("IDによる選手の検索_IDが存在しない場合に適切に例外処理されること")
  void getPlayer_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectPlayer(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getPlayer(1));
  }

  @Test
  @DisplayName("IDによる試合結果の検索_リポジトリが適切に処理されること")
  void getGameResult() throws ResourceNotFoundException {
    GameResult gameResult = new GameResult(1, 1, 2, 1, 1, 1, 1, LocalDate.now(), 1);
    when(repository.selectGameResult(1)).thenReturn(Optional.of(gameResult));
    GameResult actual = sut.getGameResult(1);
    verify(repository, times(1)).selectGameResult(1);
  }

  @Test
  @DisplayName("IDによる試合結果の検索_IDが存在しない場合に適切に例外処理されること")
  void getGameResult_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectGameResult(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getGameResult(1));
  }

  @Test
  @DisplayName("IDによる選手試合成績の検索_リポジトリが適切に処理されること")
  void getPlayerGameStat() throws ResourceNotFoundException {
    PlayerGameStat playerGameStat = new PlayerGameStat(1, 1, 1, 1, 1, true, 1, 1, 1, 1, 1);
    when(repository.selectPlayerGameStat(1)).thenReturn(Optional.of(playerGameStat));
    PlayerGameStat actual = sut.getPlayerGameStat(1);
    verify(repository, times(1)).selectPlayerGameStat(1);
  }

  @Test
  @DisplayName("IDによる選手試合成績の検索_IDが存在しない場合に適切に例外処理されること")
  void getPlayerGameStat_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectPlayerGameStat(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getPlayerGameStat(1));
  }

  @Test
  @DisplayName("選手IDによる選手試合成績の検索_リポジトリが適切に処理されること")
  void getPlayerGameStatsByPlayer() {
    List<PlayerGameStat> playerGameStats = sut.getPlayerGameStatsByPlayer(1);
    verify(repository, times(1)).selectPlayerGameStatsByPlayer(1);
  }

  @Test
  @DisplayName("クラブIDとシーズンIDによる選手試合成績の検索_リポジトリが適切に処理されること")
  void getGameResultsByClubAndSeason() {
    List<GameResult> gameResults = sut.getGameResultsByClubAndSeason(1, 1);
    verify(repository, times(1)).selectGameResultsByClubAndSeason(1, 1);
  }

  @Test
  @DisplayName("クラブIDによる選手の検索_リポジトリが適切に処理されること")
  void getPlayersByClub() {
    List<Player> players = sut.getPlayersByClub(1);
    verify(repository, times(1)).selectPlayersByClub(1);
  }

  @Test
  @DisplayName("リーグIDによるクラブの検索_リポジトリが適切に処理されること")
  void getClubsByLeague() {
    List<Club> clubs = sut.getClubsByLeague(1);
    verify(repository, times(1)).selectClubsByLeague(1);
  }

  @Test
  @DisplayName("国IDによるリーグの検索_リポジトリが適切に処理されること")
  void getLeaguesByCountry() {
    List<League> leagues = sut.getLeaguesByCountry(1);
    verify(repository, times(1)).selectLeaguesByCountry(1);
  }

  @Test
  @DisplayName("国すべての検索_リポジトリが適切に処理されること")
  void getCountries() {
    List<Country> countries = sut.getCountries();
    verify(repository, times(1)).selectCountries();
  }

  @Test
  @DisplayName("シーズンすべての検索_リポジトリが適切に処理されること")
  void getSeasons() {
    List<Season> seasons = sut.getSeasons();
    verify(repository, times(1)).selectSeasons();
  }

  @Test
  @DisplayName("選手の更新_リポジトリが適切に処理されること")
  void updatePlayer() throws FootballException, ResourceNotFoundException {
    PlayerForJson playerForJson = new PlayerForJson(1, "sampleName", 1);
    Player player = new Player(playerForJson);
    when(repository.selectPlayer(player.getId())).thenReturn(Optional.of(new Player(1, 1, "oldName", 1)));
    sut.updatePlayer(player);
    verify(repository, times(1)).selectPlayersByClub(player.getClubId());
    verify(repository, times(1)).selectPlayer(player.getId());
    verify(repository, times(1)).updatePlayer(player);
  }

  @Test
  @DisplayName("選手の更新_numberが重複している場合に適切に例外処理されること")
  void updatePlayer_withDuplicatedNumber() {
    PlayerForJson playerForJson = new PlayerForJson(1, "sampleName", 1);
    Player player = new Player(playerForJson);
    // 既に同じ背番号の選手が登録されている状態を作る
    when(repository.selectPlayersByClub(1)).thenReturn(List.of(
        new Player(1, 1, "sampleName", 1)
    ));
    // 例外が投げられることを確認し、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.updatePlayer(player));
    assertEquals("Player number is already used", thrown.getMessage());
  }

  @Test
  @DisplayName("選手の更新_更新対象の選手が存在しない場合に適切に例外処理されること")
  void updatePlayer_withNotFound() {
    PlayerForJson playerForJson = new PlayerForJson(1, "sampleName", 1);
    Player player = new Player(playerForJson);
    // リポジトリが空の状態を作る
    when(repository.selectPlayer(player.getId())).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.updatePlayer(player));
  }

  @Test
  @DisplayName("シーズンの過去シーズンへの更新_リポジトリが適切に処理されること")
  void updateSeasonsCurrentFalse() {
    sut.updateSeasonsCurrentFalse();
    verify(repository, times(1)).updateSeasonsCurrentFalse();
  }

  @Test
  @DisplayName("選手とシーズンによる選手試合成績の検索_リポジトリが適切に処理されること")
  void getPlayerGameStatsByPlayerAndSeason() throws ResourceNotFoundException {
    when(repository.selectPlayer(1)).thenReturn(Optional.of(new Player(1, 1, "sampleName", 1)));
    List<PlayerGameStat> playerGameStats = sut.getPlayerGameStatsByPlayerAndSeason(1, 1);
    verify(repository, times(1)).selectPlayer(1);
    verify(repository, times(1)).selectPlayerGameStatsByPlayerAndSeason(1, 1);
  }

  @Test
  @DisplayName("選手とシーズンによる選手試合成績の検索_選手が存在しない場合に適切に例外処理されること")
  void getPlayerGameStatsByPlayerAndSeason_withNotFoundPlayer() {
    // リポジトリが空の状態を作る
    when(repository.selectPlayer(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getPlayerGameStatsByPlayerAndSeason(1, 1));
  }

  @Test
  @DisplayName("クラブIDによる選手試合成績の検索_リポジトリが適切に処理されること")
  void getPlayerSeasonStatsByClubId() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("選手IDによる選手シーズン成績の検索_リポジトリが適切に処理されること")
  void getPlayerSeasonStatByPlayerId() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("選手IDによる選手試合成績の検索_リポジトリが適切に処理されること")
  void getPlayerSeasonStatsByPlayerId() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("出場選手のみの選手試合成績の検索_リポジトリが適切に処理されること")
  void getPlayerGameStatsExceptAbsent() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("選手試合成績からの得点の取得_リポジトリが適切に処理されること")
  void getScoreByPlayerGameStats() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("勝利クラブIDの取得_リポジトリが適切に処理されること")
  void getWinnerClubId() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("試合結果と選手成績の登録_リポジトリが適切に処理されること")
  void registerGameResultAndPlayerGameStats() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("日付の型変換_リポジトリが適切に処理されること")
  void convertStringToLocalDate() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("選手試合成績の登録用に変換_リポジトリが適切に処理されること")
  void convertPlayerGameStatsForInsertToPlayerGameStats() {
    // 現状テスト項目なし
  }

  @Test
  @DisplayName("試合結果を選手成績とともに取得_リポジトリが適切に処理されること")
  void getGameResultWithPlayerStats() throws ResourceNotFoundException {
    int gameId = 1;
    when(repository.selectGameResult(gameId)).thenReturn(Optional.of(new GameResult(1, 1, 2, 1, 1, 1, 1, LocalDate.now(), 1)));
    GameResultWithPlayerStats gameResultWithPlayerStats = sut.getGameResultWithPlayerStats(1);
    verify(repository, times(1)).selectPlayerGameStatsByGame(gameId);
  }

  @Test
  @DisplayName("試合結果を選手成績とともに取得_試合結果が存在しない場合に適切に例外処理されること")
  void getGameResultWithPlayerStats_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectGameResult(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getGameResultWithPlayerStats(1));
  }

  @Test
  @DisplayName("現在のシーズンが取得できること")
  void getCurrentSeason() throws ResourceNotFoundException {
    when(repository.selectCurrentSeason()).thenReturn(Optional.of(new Season(1, "2000-01", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30), true)));
    Season actual = sut.getCurrentSeason();
    verify(repository, times(1)).selectCurrentSeason();
  }

  @Test
  @DisplayName("現在のシーズンが取得できること_存在しない場合に適切に例外処理されること")
  void getCurrentSeason_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectCurrentSeason()).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getCurrentSeason());
  }
}