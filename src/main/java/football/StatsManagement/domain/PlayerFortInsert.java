package football.StatsManagement.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerFortInsert {
  @NotNull
  private int clubId;

  @NotBlank
  private String alphabetName;

  @NotBlank
  private String kanaName;

  @NotNull
  private int number;

}
