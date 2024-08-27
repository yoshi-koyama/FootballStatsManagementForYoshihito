package football.StatsManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerGameStat {
  private int id;
  private int playerId;
  private int clubId;
  private boolean starter;
  private int goals;
  private int assists;
  private int minutes;
  private int yellowCards;
  private int redCards;
  private LocalDate gameDate;
  private boolean absent;

  private String playerName;
}
