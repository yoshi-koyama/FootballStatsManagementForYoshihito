package football.StatsManagement.model.domain.json;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerForTransfer {
  @Positive
  private int clubId;

  @Positive
  private int number;
}
