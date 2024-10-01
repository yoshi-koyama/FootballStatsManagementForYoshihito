package football.StatsManagement.model.domain;

import football.StatsManagement.model.data.Player;
import football.StatsManagement.model.data.PlayerGameStat;
import java.util.List;

public record PlayerSeasonStat(
    int playerId,
    List<PlayerGameStat> playerGameStats,
    int seasonId,
    int clubId,
    int games,
    int starterGames,
    int substituteGames,
    int goals,
    int assists,
    int minutes,
    int yellowCards,
    int redCards
) {

  public static PlayerSeasonStat initialPlayerSeasonStat(int playerId, List<PlayerGameStat> playerGameStats, int seasonId, int clubId) {
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

    return new PlayerSeasonStat(playerId, playerGameStats, seasonId, clubId, games, starterGames, substituteGames, goals, assists, minutes, yellowCards, redCards);
  }
}
