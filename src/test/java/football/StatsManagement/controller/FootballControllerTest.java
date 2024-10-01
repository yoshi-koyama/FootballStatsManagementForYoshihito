package football.StatsManagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import football.StatsManagement.model.data.Country;
import football.StatsManagement.model.data.League;
import football.StatsManagement.model.domain.Standing;
import football.StatsManagement.service.FootballService;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(FootballController.class)
class FootballControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FootballService service;

  @Test
  @DisplayName("IDを指定して国を取得できること")
  void getCountry() throws Exception {
    int id = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/countries/" + id))
        .andExpect(status().isOk());
    verify(service, times(1)).getCountry(id);
  }

  @Test
  @DisplayName("IDを指定して国を取得する際にIDが0以下の場合、400エラーが返却されること")
  void getCountryWithInvalidId() throws Exception {
    int id = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/countries/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("IDを指定してリーグを取得できること")
  void getLeague() throws Exception {
    int id = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/leagues/" + id))
        .andExpect(status().isOk());
    verify(service, times(1)).getLeague(id);
  }

  @Test
  @DisplayName("IDを指定してリーグを取得する際にIDが0以下の場合、400エラーが返却されること")
  void getLeagueWithInvalidId() throws Exception {
    int id = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/leagues/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("IDを指定してクラブを取得できること")
  void getClub() throws Exception {
    int id = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/clubs/" + id))
        .andExpect(status().isOk());
    verify(service, times(1)).getClub(id);
  }

  @Test
  @DisplayName("IDを指定してクラブを取得する際にIDが0以下の場合、400エラーが返却されること")
  void getClubWithInvalidId() throws Exception {
    int id = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/clubs/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("IDを指定して選手を取得できること")
  void getPlayer() throws Exception {
    int id = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/players/" + id))
        .andExpect(status().isOk());
    verify(service, times(1)).getPlayer(id);
  }

  @Test
  @DisplayName("IDを指定して選手を取得する際にIDが0以下の場合、400エラーが返却されること")
  void getPlayerWithInvalidId() throws Exception {
    int id = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/players/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("国一覧を取得できること")
  void getCountries() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/countries"))
        .andExpect(status().isOk());
    verify(service, times(1)).getCountries();
  }

  @Test
  @DisplayName("国IDに紐づくリーグ一覧を取得できること")
  void getLeagues() throws Exception {
    int countryId = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/countries/" + countryId + "/leagues"))
        .andExpect(status().isOk());
    verify(service, times(1)).getLeaguesByCountry(countryId);
  }

  @Test
  @DisplayName("国IDに紐づくリーグ一覧を取得する際にIDが0以下の場合、400エラーが返却されること")
  void getLeaguesWithInvalidCountryId() throws Exception {
    int countryId = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/countries/" + countryId + "/leagues"))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("リーグIDに紐づくクラブ一覧を取得できること")
  void getClubs() throws Exception {
    int leagueId = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/leagues/" + leagueId + "/clubs"))
        .andExpect(status().isOk());
    verify(service, times(1)).getClubsByLeague(leagueId);
  }

  @Test
  @DisplayName("リーグIDに紐づくクラブ一覧を取得する際にIDが0以下の場合、400エラーが返却されること")
  void getClubsWithInvalidLeagueId() throws Exception {
    int leagueId = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/leagues/" + leagueId + "/clubs"))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("リーグIDとシーズンIDに紐づく順位表を取得できること")
  void getStanding() throws Exception {
    int leagueId = 1;
    int seasonId = 1;
    MockedStatic<Standing> standing = mockStatic(Standing.class);
    mockMvc.perform(MockMvcRequestBuilders.get("/leagues/" + leagueId + "/standings/" + seasonId))
        .andExpect(status().isOk());
    standing.verify(() -> Standing.initialStanding(leagueId, seasonId, service));
  }

  @ParameterizedTest
  @CsvSource({
      "0, 1",
      "1, 0"
  })
  @DisplayName("リーグIDとシーズンIDに紐づく順位表を取得する際のIDのバリデーションテスト")
  void getStandingWithInvalidId(int leagueId, int seasonId) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/leagues/" + leagueId + "/standings/" + seasonId))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("クラブIDに紐づく選手一覧を取得できること")
  void getPlayers() throws Exception {
    int clubId = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/clubs/" + clubId + "/players"))
        .andExpect(status().isOk());
    verify(service, times(1)).getPlayersByClub(clubId);
  }

  @Test
  @DisplayName("クラブIDに紐づく選手一覧を取得する際にIDが0以下の場合、400エラーが返却されること")
  void getPlayersWithInvalidClubId() throws Exception {
    int clubId = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/clubs/" + clubId + "/players"))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("選手IDとシーズンIDに紐づく選手の試合成績を取得できること")
  void getPlayerGameStatsBySeason() throws Exception {
    int playerId = 1;
    int seasonId = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/players/" + playerId + "/player-game-stats/" + seasonId))
        .andExpect(status().isOk());
    verify(service, times(1)).getPlayerGameStatsByPlayerAndSeason(playerId, seasonId);
  }

  @ParameterizedTest
  @CsvSource({
      "0, 1",
      "1, 0"
  })
  @DisplayName("選手IDとシーズンIDに紐づく選手の試合成績を取得する際のIDのバリデーションテスト")
  void getPlayerGameStatsBySeasonWithInvalidId(int playerId, int seasonId) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/players/" + playerId + "/player-game-stats/" + seasonId))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("クラブIDとシーズンIDに紐づく選手のシーズン成績を取得できること")
  void getPlayerSeasonStatsByClubId() throws Exception {
    int clubId = 1;
    int seasonId = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/clubs/" + clubId + "/players-season-stats/" + seasonId))
        .andExpect(status().isOk());
    verify(service, times(1)).getPlayerSeasonStatsByClubId(clubId, seasonId);
  }

  @ParameterizedTest
  @CsvSource({
      "0, 1",
      "1, 0"
  })
  @DisplayName("クラブIDとシーズンIDに紐づく選手のシーズン成績を取得する際のIDのバリデーションテスト")
  void getPlayerSeasonStatsByClubIdWithInvalidId(int clubId, int seasonId) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/clubs/" + clubId + "/players-season-stats/" + seasonId))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("選手IDとシーズンIDに紐づく選手のシーズン成績を取得できること")
  void getPlayerSeasonStat() throws Exception {
    int playerId = 1;
    int seasonId = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/players/" + playerId + "/player-season-stat/" + seasonId))
        .andExpect(status().isOk());
    verify(service, times(1)).getPlayerSeasonStatByPlayerId(playerId, seasonId);
  }

  @ParameterizedTest
  @CsvSource({
      "0, 1",
      "1, 0"
  })
  @DisplayName("選手IDとシーズンIDに紐づく選手のシーズン成績を取得する際のIDのバリデーションテスト")
  void getPlayerSeasonStatWithInvalidId(int playerId, int seasonId) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/players/" + playerId + "/player-season-stat/" + seasonId))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("選手IDに紐づく選手の通算成績を取得できること")
  void getPlayerSeasonStatsByPlayerId() throws Exception {
    int playerId = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/players/" + playerId + "/player-season-stats"))
        .andExpect(status().isOk());
    verify(service, times(1)).getPlayerSeasonStatsByPlayerId(playerId);
  }

  @Test
  @DisplayName("選手IDに紐づく選手の通算成績を取得する際にIDが0以下の場合、400エラーが返却されること")
  void getPlayerSeasonStatsByPlayerIdWithInvalidId() throws Exception {
    int playerId = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/players/" + playerId + "/player-season-stats"))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("試合IDに紐づく試合結果を取得できること")
  void getGameResult() throws Exception {
    int id = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/game-results/" + id))
        .andExpect(status().isOk());
    verify(service, times(1)).getGameResult(id);
  }

  @Test
  @DisplayName("試合IDに紐づく試合結果を取得する際にIDが0以下の場合、400エラーが返却されること")
  void getGameResultWithInvalidId() throws Exception {
    int id = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/game-results/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("国の登録ができること")
  void registerCountry() throws Exception {
    String name = "Sample Country";
    mockMvc.perform(MockMvcRequestBuilders.post("/country")
        .param("name", name))
        .andExpect(status().isOk());
    verify(service, times(1)).registerCountry(any(Country.class));
  }

  @Test
  @DisplayName("国の登録の際に国名が空文字の場合、400エラーが返却されること")
  void registerCountryWithEmptyName() throws Exception {
    String name = "";
    mockMvc.perform(MockMvcRequestBuilders.post("/country")
        .param("name", name))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  @DisplayName("リーグの登録ができること")
  void registerLeague() throws Exception {
    String requestBody = """
        {
          "name": "Sample League",
          "countryId": 1
        }
        """;
    mockMvc.perform(MockMvcRequestBuilders.post("/league")
        .contentType("application/json")
        .content(requestBody))
        .andExpect(status().isOk());
    verify(service, times(1)).registerLeague(any(League.class));
  }

  @Test
  @DisplayName("リーグの登録の際にバリデーションエラーが発生すること")
  void registerLeagueWithInvalidRequest() throws Exception {
    String requestBody = """
        {
          "name": "",
          "countryId": 0
        }
        """;
    mockMvc.perform(MockMvcRequestBuilders.post("/league")
        .contentType("application/json")
        .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(result -> {
          MethodArgumentNotValidException ex = (MethodArgumentNotValidException) result.getResolvedException();

          BindingResult bindingResult = ex.getBindingResult();
          List<FieldError> fieldErrors = bindingResult.getFieldErrors();

          assertEquals(2, fieldErrors.size());

          // エラーメッセージの順序がどちらになるかわからないため、どちらが来ても検証する
          boolean nameErrorPresent = false;
          boolean countryIdErrorPresent = false;

          for (FieldError fieldError : fieldErrors) {
            if (fieldError.getField().equals("name")) {
              nameErrorPresent = true;
              assertEquals("must not be blank", fieldError.getDefaultMessage());
            } else if (fieldError.getField().equals("countryId")) {
              countryIdErrorPresent = true;
              assertEquals("must be greater than 0", fieldError.getDefaultMessage());
            }
          }

          assertTrue(nameErrorPresent, "Expected validation error for 'name' field");
          assertTrue(countryIdErrorPresent, "Expected validation error for 'countryId' field");

        });
  }


  @Test
  @DisplayName("クラブの登録ができること")
  void registerClub() throws Exception {
    String requestBody = """
        {
          "name": "Sample Club",
          "leagueId": 1
        }
        """;
    mockMvc.perform(MockMvcRequestBuilders.post("/club")
        .contentType("application/json")
        .content(requestBody))
        .andExpect(status().isOk());
    verify(service, times(1)).registerClub(any());
  }

  @Test
  @DisplayName("クラブの登録の際にバリデーションエラーが発生すること")
  void registerClubWithInvalidRequest() throws Exception {
    String requestBody = """
        {
          "name": "",
          "leagueId": 0
        }
        """;
    mockMvc.perform(MockMvcRequestBuilders.post("/club")
        .contentType("application/json")
        .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(result -> {
          MethodArgumentNotValidException ex = (MethodArgumentNotValidException) result.getResolvedException();

          BindingResult bindingResult = ex.getBindingResult();
          List<FieldError> fieldErrors = bindingResult.getFieldErrors();

          assertEquals(2, fieldErrors.size());

          // エラーメッセージの順序がどちらになるかわからないため、どちらが来ても検証する
          boolean nameErrorPresent = false;
          boolean leagueIdErrorPresent = false;

          for (FieldError fieldError : fieldErrors) {
            if (fieldError.getField().equals("name")) {
              nameErrorPresent = true;
              assertEquals("must not be blank", fieldError.getDefaultMessage());
            } else if (fieldError.getField().equals("leagueId")) {
              leagueIdErrorPresent = true;
              assertEquals("must be greater than 0", fieldError.getDefaultMessage());
            }
          }

          assertTrue(nameErrorPresent, "Expected validation error for 'name' field");
          assertTrue(leagueIdErrorPresent, "Expected validation error for 'leagueId' field");

        });
  }

  @Test
  @DisplayName("選手の登録ができること")
  void registerPlayer() throws Exception {
    String requestBody = """
        {
          "name": "Sample Player",
          "clubId": 1,
          "number": 1
        }
        """;
    mockMvc.perform(MockMvcRequestBuilders.post("/player")
        .contentType("application/json")
        .content(requestBody))
        .andExpect(status().isOk());
    verify(service, times(1)).registerPlayer(any());
  }

  @Test
  @DisplayName("選手の登録の際にバリデーションエラーが発生すること")
  void registerPlayerWithInvalidRequest() throws Exception {
    String requestBody = """
        {
          "name": "",
          "clubId": 0,
          "number": 0
        }
        """;
    mockMvc.perform(MockMvcRequestBuilders.post("/player")
        .contentType("application/json")
        .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(result -> {
          MethodArgumentNotValidException ex = (MethodArgumentNotValidException) result.getResolvedException();

          BindingResult bindingResult = ex.getBindingResult();
          List<FieldError> fieldErrors = bindingResult.getFieldErrors();

          assertEquals(3, fieldErrors.size());

          // エラーメッセージの順序がどちらになるかわからないため、どちらが来ても検証する
          boolean nameErrorPresent = false;
          boolean clubIdErrorPresent = false;
          boolean numberErrorPresent = false;

          for (FieldError fieldError : fieldErrors) {
            if (fieldError.getField().equals("name")) {
              nameErrorPresent = true;
              assertEquals("must not be blank", fieldError.getDefaultMessage());
            } else if (fieldError.getField().equals("clubId")) {
              clubIdErrorPresent = true;
              assertEquals("must be greater than 0", fieldError.getDefaultMessage());
            } else if (fieldError.getField().equals("number")) {
              numberErrorPresent = true;
              assertEquals("must be greater than 0", fieldError.getDefaultMessage());
            }
          }

          assertTrue(nameErrorPresent, "Expected validation error for 'name' field");
          assertTrue(clubIdErrorPresent, "Expected validation error for 'clubId' field");
          assertTrue(numberErrorPresent, "Expected validation error for 'number' field");

        });
  }

  @Test
  @DisplayName("試合結果の登録ができること")
  void registerGameResult() {
  }

  @Test
  void registerSeason() {
  }

  @Test
  void updatePlayer() {
  }
}