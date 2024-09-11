package football.StatsManagement.model.data;

import football.StatsManagement.model.domain.json.SeasonForJson;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class Season {
  private int id;
  private String name;
  private LocalDate startDate;
  private LocalDate endDate;

  // @Insert用
  public Season(SeasonForJson seasonForJson) {
    this.id = 0;
    this.name = seasonForJson.getName();
    this.startDate = seasonForJson.getStartDate();
    this.endDate = seasonForJson.getEndDate();
  }
}
