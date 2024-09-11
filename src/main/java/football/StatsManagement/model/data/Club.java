package football.StatsManagement.model.data;

import football.StatsManagement.model.domain.json.ClubForJson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class Club {
  private final int id;
  private int leagueId;
  private String name;

  // @Insert用
  public Club(ClubForJson clubForJson) {
    this.id = 0;
    this.leagueId = clubForJson.getLeagueId();
    this.name = clubForJson.getName();
  }
}
