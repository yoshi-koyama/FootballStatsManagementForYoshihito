package football.StatsManagement.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class League {
  private final int id;

  @NotNull
  private int countryId;

  @NotBlank
  private String name;

  // @Insert用
  public League(int countryId, String name) {
    this.id = 0;
    this.countryId = countryId;
    this.name = name;
  }
}
