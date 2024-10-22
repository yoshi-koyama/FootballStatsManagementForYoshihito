package football.StatsManagement.model.domain;

import football.StatsManagement.exception.ResourceNotFoundException;
import football.StatsManagement.service.FootballService;
import football.StatsManagement.model.data.Club;
import football.StatsManagement.utils.RankingUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Standing {
    private final int leagueId;
    private final int seasonId;
    private final List<ClubForStanding> clubForStandings;
    private final String leagueName;
    private final String seasonName;

    public Standing(
      int leagueId,
      int seasonId,
      List<ClubForStanding> clubForStandings,
      String leagueName,
      String seasonName) {
        this.leagueId = leagueId;
        this.seasonId = seasonId;
        this.clubForStandings = clubForStandings;
        this.leagueName = leagueName;
        this.seasonName = seasonName;
    }

    public static Standing initialStanding(int leagueId, int seasonId, FootballService service) throws ResourceNotFoundException {
        System.out.println("initialStandingを開始");
        List<Club> clubs = service.getClubsByLeague(leagueId);
        // リーグによって異なる順位決定方法
        List<ClubForStanding> rankedClubForStandings = rankedClubsForStanding(clubs, seasonId, leagueId, service);
        // 順位を設定
        for (int i = 0; i < rankedClubForStandings.size(); i++) {
            rankedClubForStandings.get(i).setPosition(i + 1);
        }
        String leagueName = service.getLeague(leagueId).getName();
        String seasonName = service.getSeason(seasonId).getName();
        return new Standing(leagueId, seasonId, rankedClubForStandings, leagueName, seasonName);
    }

    public static Standing initialStanding2() {
        System.out.println("initialStanding2内の処理を開始");
        return new Standing(1, 100001, new ArrayList<>(), "league1", "1000-01");
    }

    public static String initialStanding3() {
        return "initialStanding3";
    }

    private static List<ClubForStanding> rankedClubsForStanding(List<Club> clubs, int seasonId, int leagueId, FootballService service) {
        List<ClubForStanding> clubForStandings = clubs.stream()
          .map(club -> ClubForStanding.initialClubForStanding(seasonId, club, service))
          .toList();

        return RankingUtils.sortedClubForStandings(leagueId, clubForStandings);
    }

    // テスト用にequalsとhashCodeをオーバーライド
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Standing that = (Standing) o;

        return leagueId == that.leagueId &&
          seasonId == that.seasonId &&
          leagueName.equals(that.leagueName) &&
          seasonName.equals(that.seasonName) &&
          clubForStandings.equals(that.clubForStandings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leagueId, seasonId, clubForStandings, leagueName, seasonName);
    }

    public int leagueId() {
        return leagueId;
    }

    public int seasonId() {
        return seasonId;
    }

    public List<ClubForStanding> clubForStandings() {
        return clubForStandings;
    }

    public String leagueName() {
        return leagueName;
    }

    public String seasonName() {
        return seasonName;
    }

    @Override
    public String toString() {
        return "Standing[" +
          "leagueId=" + leagueId + ", " +
          "seasonId=" + seasonId + ", " +
          "clubForStandings=" + clubForStandings + ", " +
          "leagueName=" + leagueName + ", " +
          "seasonName=" + seasonName + ']';
    }

}

