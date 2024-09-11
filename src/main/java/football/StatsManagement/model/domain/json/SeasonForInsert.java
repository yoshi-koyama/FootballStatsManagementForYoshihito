package football.StatsManagement.model.domain.json;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeasonForInsert {
  @NotBlank
  private String name;
  @NotBlank
  private LocalDate startDate;
  @NotBlank
  private LocalDate endDate;
}
