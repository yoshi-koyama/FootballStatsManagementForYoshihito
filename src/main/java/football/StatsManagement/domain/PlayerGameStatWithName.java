package football.StatsManagement.domain;

import football.StatsManagement.data.PlayerGameStat;
import lombok.Getter;

@Getter
public class PlayerGameStatWithName {
  private final int id;
  private final int gameId;
  private final int playerId;
  private final int clubId;
  private final int number;
  private final boolean starter;
  private final int goals;
  private final int assists;
  private final int minutes;
  private final int yellowCards;
  private final int redCards;
  private final String playerAlphabetName;
  private final String playerKanaName;



  public PlayerGameStatWithName(PlayerGameStat playerGameStat, String playerAlphabetName, String playerKanaName) {
    this.id = playerGameStat.getId();
    this.gameId = playerGameStat.getGameId();
    this.playerId = playerGameStat.getPlayerId();
    this.clubId = playerGameStat.getClubId();
    this.number = playerGameStat.getNumber();
    this.starter = playerGameStat.isStarter();
    this.goals = playerGameStat.getGoals();
    this.assists = playerGameStat.getAssists();
    this.minutes = playerGameStat.getMinutes();
    this.yellowCards = playerGameStat.getYellowCards();
    this.redCards = playerGameStat.getRedCards();
    this.playerAlphabetName = playerAlphabetName;
    this.playerKanaName = playerKanaName;
  }

}
