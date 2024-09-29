package football.StatsManagement.model.domain;

import football.StatsManagement.exception.ResourceNotFoundException;
import football.StatsManagement.model.data.Player;
import football.StatsManagement.model.data.PlayerGameStat;
import football.StatsManagement.service.FootballService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerSeasonStat {
  private Player player;
  private List<PlayerGameStat> playerGameStats;

  private int seasonId;
  private int clubId;

  private int games;
  private int starterGames;
  private int substituteGames;
  private int goals;
  private int assists;
  private int minutes;
  private int yellowCards;
  private int redCards;

  public static PlayerSeasonStat initialPlayerSeasonStat(Player player, List<PlayerGameStat> playerGameStats, int seasonId, int clubId) {
    List<PlayerGameStat> playerGameStatsByClub = playerGameStats.stream()
        .filter(playerGameStat -> playerGameStat.getClubId() == clubId)
        .toList();
    int games = playerGameStatsByClub.size();
    int starterGames = (int) playerGameStatsByClub.stream()
        .filter(PlayerGameStat::isStarter)
        .count();
    int substituteGames = games - starterGames;
    int goals = playerGameStatsByClub.stream()
        .mapToInt(PlayerGameStat::getGoals)
        .sum();
    int assists = playerGameStatsByClub.stream()
        .mapToInt(PlayerGameStat::getAssists)
        .sum();
    int minutes = playerGameStatsByClub.stream()
        .mapToInt(PlayerGameStat::getMinutes)
        .sum();
    int yellowCards = playerGameStatsByClub.stream()
        .mapToInt(PlayerGameStat::getYellowCards)
        .sum();
    int redCards = playerGameStatsByClub.stream()
        .mapToInt(PlayerGameStat::getRedCards)
        .sum();

    return new PlayerSeasonStat(player, playerGameStats, seasonId, clubId, games, starterGames, substituteGames, goals, assists, minutes, yellowCards, redCards);
  }
}
