package football.StatsManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import football.StatsManagement.exception.FootballException;
import football.StatsManagement.exception.ResourceConflictException;
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
    GameResultForJson gameResultForJson = new GameResultForJson(1, 1, 1, 1, 1, LocalDate.now(), 1);
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
  @DisplayName("シーズン登録で、シーズン名の形式が不正である場合に適切に例外処理されること")
  void registerSeason_withInvalidName() {
    SeasonForJson seasonForJson = new SeasonForJson("2000-2001", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30));
    Season season = new Season(seasonForJson);
    // 例外が投げられることを確認し、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerSeason(season));

    // 期待される例外メッセージを確認
    assertEquals("Season name should be in the format of \'yyyy-yy\'", thrown.getMessage());
  }

  @Test
  @DisplayName("シーズン登録で、シーズン名の数字が不正である場合に適切に例外処理されること")
  void registerSeason_withInvalidYear() {
    SeasonForJson seasonForJson = new SeasonForJson("2000-02", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30));
    Season season = new Season(seasonForJson);
    // 例外が投げられることを確認し、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerSeason(season));

    // 期待される例外メッセージを確認
    assertEquals("Year in season name is not correct", thrown.getMessage());
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
    PlayerGameStat playerGameStat = new PlayerGameStat(1, 1, 1, 1, true, 1, 1, 1, 1, 1, 1);
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
  @DisplayName("シーズンの過去シーズンへの更新_リポジトリが適切に処理されること")
  void updateSeasonsCurrentFalse() {
    sut.updateSeasonsCurrentFalse();
    verify(repository, times(1)).updateSeasonsCurrentFalse();
  }

  @Test
  @DisplayName("選手の背番号と名前の更新_リポジトリが適切に処理されること")
  void updatePlayerNumberAndName() throws ResourceNotFoundException, FootballException, ResourceConflictException {
    int id = 1;
    int number = 99;
    String name = "newName";
    when(repository.selectPlayer(id)).thenReturn(Optional.of(new Player(id, 1, "sampleName", 1)));
    sut.updatePlayerNumberAndName(id, number, name);
    verify(repository, times(1)).selectPlayer(id);
    verify(repository, times(1)).updatePlayerNumberAndName(id, number, name);
  }

  @Test
  @DisplayName("選手の背番号と名前の更新_選手が存在しない場合に適切に例外処理されること")
  void updatePlayerNumberAndName_withNotFoundPlayer() {
    // リポジトリが空の状態を作る
    when(repository.selectPlayer(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.updatePlayerNumberAndName(1, 1, "sampleName"));
  }

  @Test
  @DisplayName("選手の背番号と名前の更新_背番号が重複している場合に適切に例外処理されること")
  void updatePlayerNumberAndName_withDuplicatedNumber() {
    when(repository.selectPlayer(1)).thenReturn(Optional.of(new Player(1, 1, "sampleName", 1)));
    // 既に同じ背番号の選手が登録されている状態を作る
    when(sut.getPlayersByClub(1)).thenReturn(List.of(
        new Player(2, 1, "sampleName", 1)
    ));
    // 例外が投げられることを確認
    assertThrows(FootballException.class, () -> sut.updatePlayerNumberAndName(1, 1, "sampleName"));
  }

  @Test
  @DisplayName("選手の背番号と名前の更新_変更がない場合に適切に例外処理されること")
  void updatePlayerNumberAndName_withNoChange() {
    when(repository.selectPlayer(1)).thenReturn(Optional.of(new Player(1, 1, "sampleName", 1)));
    // 例外が投げられることを確認
    assertThrows(
        ResourceConflictException.class, () -> sut.updatePlayerNumberAndName(1, 1, "sampleName"));
  }

  @Test
  @DisplayName("選手のクラブと背番号の更新_リポジトリが適切に処理されること")
  void updatePlayerClubAndNumber() throws ResourceNotFoundException, FootballException, ResourceConflictException {
    int id = 1;
    int clubId = 2;
    int number = 99;
    when(repository.selectPlayer(id)).thenReturn(Optional.of(new Player(id, 1, "sampleName", 1)));
    sut.updatePlayerClubAndNumber(id, clubId, number);
    verify(repository, times(1)).selectPlayer(id);
    verify(repository, times(1)).updatePlayerClubAndNumber(id, clubId, number);
  }

  @Test
  @DisplayName("選手のクラブと背番号の更新_選手が存在しない場合に適切に例外処理されること")
  void updatePlayerClubAndNumber_withNotFoundPlayer() {
    // リポジトリが空の状態を作る
    when(repository.selectPlayer(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.updatePlayerClubAndNumber(1, 1, 1));
  }

  @Test
  @DisplayName("選手のクラブと背番号の更新_クラブに変更がない場合に適切に例外処理されること")
  void updatePlayerClubAndNumber_withNoChange() {
    when(repository.selectPlayer(1)).thenReturn(Optional.of(new Player(1, 1, "sampleName", 1)));
    // 例外が投げられることを確認
    assertThrows(
        ResourceConflictException.class, () -> sut.updatePlayerClubAndNumber(1, 1, 1));
  }

  @Test
  @DisplayName("選手のクラブと背番号の更新_背番号が重複している場合に適切に例外処理されること")
  void updatePlayerClubAndNumber_withDuplicatedNumber() {
    when(repository.selectPlayer(1)).thenReturn(Optional.of(new Player(1, 1, "sampleName", 1)));
    // 既に同じ背番号の選手が登録されている状態を作る
    when(sut.getPlayersByClub(2)).thenReturn(List.of(
        new Player(2, 2, "sampleName", 1)
    ));
    // 例外が投げられることを確認
    assertThrows(FootballException.class, () -> sut.updatePlayerClubAndNumber(1, 2, 1));
  }

  @Test
  @DisplayName("クラブのリーグの更新_リポジトリが適切に処理されること")
  void updateClubLeague() throws ResourceNotFoundException, ResourceConflictException {
    int id = 1;
    int leagueId = 2;
    when(repository.selectClub(id)).thenReturn(Optional.of(new Club(id, 1, "sampleName")));
    sut.updateClubLeague(id, leagueId);
    verify(repository, times(1)).selectClub(id);
    verify(repository, times(1)).updateClubLeague(id, leagueId);
  }

  @Test
  @DisplayName("クラブのリーグの更新_クラブが存在しない場合に適切に例外処理されること")
  void updateClubLeague_withNotFoundClub() {
    // リポジトリが空の状態を作る
    when(repository.selectClub(1)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.updateClubLeague(1, 1));
  }
  @Test
  @DisplayName("クラブのリーグの更新_リーグに変更がない場合に適切に例外処理されること")
  void updateClubLeague_withNoChange() {
    when(repository.selectClub(1)).thenReturn(Optional.of(new Club(1, 1, "sampleName")));
    // 例外が投げられることを確認
    assertThrows(
        ResourceConflictException.class, () -> sut.updateClubLeague(1, 1));
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
  @DisplayName("試合結果と選手成績の登録_ホームクラブのスコアが不正な場合に例外処理が発生すること")
  void registerGameResultAndPlayerGameStats_withInvalidHomeScore() throws Exception {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 2, 1, 1, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    // ホームクラブのスコアが不正な値になるようにする
    List<PlayerGameStatForJson> homeClubStatsForJson = List.of(
        new PlayerGameStatForJson(1, true, 2, 2, 1, 1, 1)
    );
    List<PlayerGameStat> homeClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForJson);
    List<PlayerGameStatForJson> awayClubStatsForJson = List.of(
        new PlayerGameStatForJson(2, false, 2, 2, 1, 1, 1)
    );
    List<PlayerGameStat> awayClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForJson);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
    // 例外が投げられることを確認、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats));
    assertEquals("Home score is not correct", thrown.getMessage());
  }

  @Test
  @DisplayName("試合結果と選手成績の登録_アウェイクラブのスコアが不正な場合に例外処理が発生すること")
  void registerGameResultAndPlayerGameStats_withInvalidAwayScore() throws Exception {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 2, 1, 1, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    List<PlayerGameStatForJson> homeClubStatsForJson = List.of(
        new PlayerGameStatForJson(1, true, 1, 2, 1, 1, 1)
    );
    List<PlayerGameStat> homeClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForJson);
    // アウェイクラブのスコアが不正な値になるようにする
    List<PlayerGameStatForJson> awayClubStatsForJson = List.of(
        new PlayerGameStatForJson(2, false, 2, 2, 1, 1, 1)
    );
    List<PlayerGameStat> awayClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForJson);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
    // 例外が投げられることを確認、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats));
    assertEquals("Away score is not correct", thrown.getMessage());
  }

  @Test
  @DisplayName("試合結果と選手成績の登録_ホームクラブのアシスト数が不正な場合に例外処理が発生すること")
  void registerGameResultAndPlayerGameStats_withInvalidHomeAssist() throws Exception {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 2, 1, 1, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    // ホームクラブのアシストが不正な値になるようにする
    List<PlayerGameStatForJson> homeClubStatsForJson = List.of(
        new PlayerGameStatForJson(1, true, 1, 2, 1, 1, 1)
    );
    List<PlayerGameStat> homeClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForJson);
    List<PlayerGameStatForJson> awayClubStatsForJson = List.of(
        new PlayerGameStatForJson(2, false, 1, 2, 1, 1, 1)
    );
    List<PlayerGameStat> awayClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForJson);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
    // 例外が投げられることを確認、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats));
    assertEquals("Home assists is more than home score", thrown.getMessage());
  }

  @Test
  @DisplayName("試合結果と選手成績の登録_アウェイクラブのアシスト数が不正な場合に例外処理が発生すること")
  void registerGameResultAndPlayerGameStats_withInvalidAwayAssist() throws Exception {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 2, 1, 1, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    List<PlayerGameStatForJson> homeClubStatsForJson = List.of(
        new PlayerGameStatForJson(1, true, 1, 1, 1, 1, 1)
    );
    List<PlayerGameStat> homeClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForJson);
    // ホームクラブのアシストが不正な値になるようにする
    List<PlayerGameStatForJson> awayClubStatsForJson = List.of(
        new PlayerGameStatForJson(2, false, 1, 2, 1, 1, 1)
    );
    List<PlayerGameStat> awayClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForJson);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
    // 例外が投げられることを確認、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats));
    assertEquals("Away assists is more than away score", thrown.getMessage());
  }

  @Test
  @DisplayName("試合結果と選手成績の登録_ホームクラブの先発選手数が不正な場合に例外処理が発生すること")
  void registerGameResultAndPlayerGameStats_withInvalidHomeStarting() throws Exception {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 2, 1, 1, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    // ホームクラブの先発選手数が不正な値になるようにする
    List<PlayerGameStatForJson> homeClubStatsForJson = List.of(
        new PlayerGameStatForJson(1, true, 1, 1, 2, 1, 1)
    );
    List<PlayerGameStat> homeClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForJson);
    List<PlayerGameStatForJson> awayClubStatsForJson = List.of(
        new PlayerGameStatForJson(2, false, 1, 1, 1, 1, 1)
    );
    List<PlayerGameStat> awayClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForJson);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
    // 例外が投げられることを確認、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats));
    assertEquals("Home starter count is not correct", thrown.getMessage());
  }

  @Test
  @DisplayName("試合結果と選手成績の登録_アウェイクラブの先発選手数が不正な場合に例外処理が発生すること")
  void registerGameResultAndPlayerGameStats_withInvalidAwayStarting() throws Exception {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 2, 11, 12, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    List<PlayerGameStatForJson> homeClubStatsForJson = List.of(
        new PlayerGameStatForJson(1, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(2, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(3, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(4, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(5, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(6, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(7, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(8, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(9, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(10, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(11, true, 1, 1, 1, 1, 1)
    );
    List<PlayerGameStat> homeClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForJson);
    // アウェイクラブの先発選手数が不正な値になるようにする
    List<PlayerGameStatForJson> awayClubStatsForJson = List.of(
        new PlayerGameStatForJson(12, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(13, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(14, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(15, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(16, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(17, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(18, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(19, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(20, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(21, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(22, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(23, true, 1, 1, 2, 1, 1)
    );
    List<PlayerGameStat> awayClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForJson);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
    // 例外が投げられることを確認、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats));
    assertEquals("Away starter count is not correct", thrown.getMessage());
  }

  @Test
  @DisplayName("試合結果と選手成績の登録_ホームクラブの出場時間が不正な場合に例外処理が発生すること")
  void registerGameResultAndPlayerGameStats_withInvalidHomeMinutes() throws Exception {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 2, 11, 11, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    // ホームクラブの出場時間が不正な値になるようにする
    List<PlayerGameStatForJson> homeClubStatsForJson = List.of(
        new PlayerGameStatForJson(1, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(2, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(3, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(4, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(5, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(6, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(7, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(8, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(9, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(10, true, 1, 1, 1, 1, 1),
        new PlayerGameStatForJson(11, true, 1, 1, 1, 1, 1)
    );
    List<PlayerGameStat> homeClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForJson);
    List<PlayerGameStatForJson> awayClubStatsForJson = List.of(
        new PlayerGameStatForJson(12, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(13, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(14, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(15, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(16, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(17, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(18, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(19, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(20, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(21, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(22, true, 1, 1, 2, 1, 1)
    );
    List<PlayerGameStat> awayClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForJson);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
    // 例外が投げられることを確認、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats));
    assertEquals("Home minutes is not correct", thrown.getMessage());
  }

  @Test
  @DisplayName("試合結果と選手成績の登録_アウェイクラブの出場時間が不正な場合に例外処理が発生すること")
  void registerGameResultAndPlayerGameStats_withInvalidAwayMinutes() throws Exception {
    GameResultForJson gameResultForJson = new GameResultForJson(1, 2, 11, 11, 1, LocalDate.now(), 1);
    GameResult gameResult = new GameResult(gameResultForJson);
    List<PlayerGameStatForJson> homeClubStatsForJson = List.of(
        new PlayerGameStatForJson(1, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(2, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(3, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(4, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(5, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(6, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(7, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(8, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(9, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(10, true, 1, 1, 90, 1, 1),
        new PlayerGameStatForJson(11, true, 1, 1, 90, 1, 1)
    );
    List<PlayerGameStat> homeClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForJson);
    // アウェイクラブの出場時間が不正な値になるようにする
    List<PlayerGameStatForJson> awayClubStatsForJson = List.of(
        new PlayerGameStatForJson(12, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(13, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(14, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(15, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(16, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(17, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(18, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(19, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(20, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(21, true, 1, 1, 2, 1, 1),
        new PlayerGameStatForJson(22, true, 1, 1, 2, 1, 1)
    );
    List<PlayerGameStat> awayClubStats = sut.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForJson);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);
    // 例外が投げられることを確認、メッセージもチェック
    FootballException thrown = assertThrows(FootballException.class, () -> sut.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats));
    assertEquals("Away minutes is not correct", thrown.getMessage());
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

  @Test
  @DisplayName("IDを指定してシーズンの取得ができること")
  void getSeason() throws ResourceNotFoundException {
    Season season = new Season(200001, "2000-01", LocalDate.of(2000, 7, 1), LocalDate.of(2001, 6, 30), true);
    when(repository.selectSeason(200001)).thenReturn(Optional.of(season));
    Season actual = sut.getSeason(200001);
    verify(repository, times(1)).selectSeason(200001);
  }

  @Test
  @DisplayName("IDを指定してシーズンの取得ができること_存在しない場合に適切に例外処理されること")
  void getSeason_withNotFound() {
    // リポジトリが空の状態を作る
    when(repository.selectSeason(200001)).thenReturn(Optional.empty());
    // 例外が投げられることを確認
    assertThrows(ResourceNotFoundException.class, () -> sut.getSeason(200001));
  }
}