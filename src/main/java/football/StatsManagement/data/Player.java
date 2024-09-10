package football.StatsManagement.data;

import football.StatsManagement.domain.PlayerFortInsert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用
public class Player {
  private final int id;
  private int clubId;
  private String alphabetName;
  private String kanaName;
  private int number;

  // @Insert用
  public Player(PlayerFortInsert playerFortInsert) {
    this.id = 0;
    this.clubId = playerFortInsert.getClubId();
    this.alphabetName = playerFortInsert.getAlphabetName();
    this.kanaName = playerFortInsert.getKanaName();
    this.number = playerFortInsert.getNumber();
  }

}
