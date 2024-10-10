package football.StatsManagement.model.domain;

import football.StatsManagement.exception.ResourceNotFoundException;
import football.StatsManagement.service.FootballService;
import football.StatsManagement.model.data.Club;
import football.StatsManagement.utils.RankingUtils;
import java.util.ArrayList;
import java.util.List;

public record Standing(
    int leagueId,
    int seasonId,
    List<ClubForStanding> clubForStandings,
    String leagueName,
    String seasonName) {

  public static Standing initialStanding(int leagueId, int seasonId, FootballService service) throws ResourceNotFoundException {
    List<Club> clubs = service.getClubsByLeague(leagueId);
    // リーグによって異なる順位決定方法
    List<ClubForStanding> clubForStandings = rankedClubsForStanding(clubs, seasonId, leagueId, service);
    // 順位を設定
    for (int i = 0; i < clubForStandings.size(); i++) {
      clubForStandings.get(i).setPosition(i + 1);
    }
    String leagueName = service.getLeague(leagueId).getName();
    String seasonName = service.getSeason(seasonId).getName();
    return new Standing(leagueId, seasonId, clubForStandings, leagueName, seasonName);
  }

  private static List<ClubForStanding> rankedClubsForStanding(List<Club> clubs, int seasonId, int leagueId, FootballService service) {
    List<ClubForStanding> clubForStandings = clubs.stream()
        .map(club -> ClubForStanding.initialClubForStanding(seasonId, club, service))
        .toList();

    List<ClubForStanding> sortedClubForStandings = new ArrayList<>();
    // プリメーラ・ディビシオン
    if (leagueId == 7) {
      sortedClubForStandings = RankingUtils.sortedClubForStandingsInPrimeraDivision(clubForStandings);
    }

    return sortedClubForStandings;
  }
}

