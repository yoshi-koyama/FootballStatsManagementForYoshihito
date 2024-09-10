package football.StatsManagement.data;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class Country {
  private final int id;

  @NotBlank
  private String name;

  // @Insert用
  public Country(String name) {
    this.id = 0;
    this.name = name;
  }
}
