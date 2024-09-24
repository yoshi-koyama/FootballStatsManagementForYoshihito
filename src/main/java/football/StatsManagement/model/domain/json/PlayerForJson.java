package football.StatsManagement.model.domain.json;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerForJson {
  @Positive
  private int clubId;

  @NotBlank
  private String name;

  private int number;

}
