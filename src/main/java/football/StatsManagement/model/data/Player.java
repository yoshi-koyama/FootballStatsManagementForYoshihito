package football.StatsManagement.model.data;

import football.StatsManagement.model.domain.json.PlayerForJson;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "選手")
@Getter
@Setter
@AllArgsConstructor // @Select用
public class Player {
  @NotNull
  @Positive
  private final int id;

  @NotNull
  @Positive
  private int clubId;

  @NotBlank
  private String alphabetName;

  @NotBlank
  private String kanaName;

  @NotNull
  @Positive
  private int number;

  // @Insert用
  public Player(PlayerForJson playerForJson) {
    this.id = 0;
    this.clubId = playerForJson.getClubId();
    this.alphabetName = playerForJson.getAlphabetName();
    this.kanaName = playerForJson.getKanaName();
    this.number = playerForJson.getNumber();
  }

}
