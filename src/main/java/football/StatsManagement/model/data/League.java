package football.StatsManagement.model.data;

import football.StatsManagement.model.domain.json.LeagueForJson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class League {
  private final int id;
  private int countryId;
  private String name;

  // @Insert用
  public League(LeagueForJson leagueForJson) {
    this.id = 0;
    this.countryId = leagueForJson.getCountryId();
    this.name = leagueForJson.getName();
  }
}
