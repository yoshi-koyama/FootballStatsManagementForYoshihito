package football.StatsManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Season {
  private int id;
  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
}
