package football.StatsManagement.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class Club {
  private final int id;

  @NotNull
  private int leagueId;

  @NotBlank
  private String name;

  // @Select用
  public Club(int leagueId, String name) {
    this.id = 0;
    this.leagueId = leagueId;
    this.name = name;
  }
}
