package football.StatsManagement.utils;

import static org.junit.jupiter.api.Assertions.*;

import football.StatsManagement.model.data.Club;
import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.domain.ClubForStanding;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;


// 未完成、テスト自体の構造を見直し予定
class RankingUtilsTest {

  @ParameterizedTest
  @MethodSource("clubForStandingProvider")
  @DisplayName("プリメーラ・ディビシオンの順位ソートが正しく行われること")
  void sortedClubForStandingsInPrimeraDivision(List<ClubForStanding> inputClubs, List<ClubForStanding> expectedClubs) {
    List<ClubForStanding> actual = RankingUtils.sortedClubForStandingsInPrimeraDivision(inputClubs);
    assertEquals(expectedClubs, actual);
  }

  // テストケースのデータ提供メソッド
  static Stream<Arguments> clubForStandingProvider() {
    Club clubA = new Club(1, 1, "Club A");
    Club clubB = new Club(2, 1, "Club B");
    Club clubC = new Club(3, 1, "Club C");

    /*
    * 基本の考え方
    * A,B,Cの3チームがあり、Cは弱い、AとBで競う
    * BがAをギリギリ上回るような状況を作る
    * */

    // ①勝ち点のみでの決定
    // 試合数：A=2, B=2, C=2
    // A:勝ち点3, B:勝ち点6, C:勝ち点0
    GameResult gameResultAvsB1 = new GameResult(1, 1, 2, 1, 2, 2, 1, LocalDate.now(), 1);
    GameResult gameResultBvsC1 = new GameResult(2, 2, 3, 2, 1, 2, 1, LocalDate.now(), 1);
    GameResult gameResultCvsA1 = new GameResult(3, 3, 1, 0, 1, 1, 1, LocalDate.now(), 1);

    List<GameResult> gameResultsA1 = List.of(gameResultAvsB1, gameResultCvsA1);
    List<GameResult> gameResultsB1 = List.of(gameResultAvsB1, gameResultBvsC1);
    List<GameResult> gameResultsC1 = List.of(gameResultBvsC1, gameResultCvsA1);

    ClubForStanding clubForStandingA1 = new ClubForStanding(gameResultsA1, clubA, 2, 1, 0, 1, 3, 2, 3, 0);
    ClubForStanding clubForStandingB1 = new ClubForStanding(gameResultsB1, clubB, 2, 1, 0, 1, 3, 3, 2, 1);
    ClubForStanding clubForStandingC1 = new ClubForStanding(gameResultsC1, clubC, 2, 1, 0, 1, 3, 3, 4, -1);

    List<ClubForStanding> input1 = List.of(clubForStandingA1, clubForStandingB1, clubForStandingC1);
    List<ClubForStanding> expected1 = List.of(clubForStandingB1, clubForStandingA1, clubForStandingC1);

    // ②当該チーム間の勝ち点


    // ③当該チーム間の得失点差


    // ④-1.全試合の得失点差-①②③が同じ場合


    // ④-2.全試合の得失点差-①が同じで試合数が2試合未満


    // ⑤全試合の得点


    // 決着がつかない場合


    // return
    return Stream.of(
        Arguments.of(input1, expected1)
    );

  }

  @Test
  void sortedClubForStandingsInEnglishPremierLeague() {
  }
}