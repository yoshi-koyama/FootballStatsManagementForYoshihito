package football.StatsManagement.model.domain.json;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeagueForJson {
  @Positive
  private int countryId;

  @NotBlank
  private String name;
}
