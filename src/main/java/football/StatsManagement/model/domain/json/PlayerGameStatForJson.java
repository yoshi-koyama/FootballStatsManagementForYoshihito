package football.StatsManagement.model.domain.json;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerGameStatForJson {
  @NotNull
  private int playerId;

  // 試合成績
  @NotNull
  private boolean starter;
  @NotNull
  private int goals;
  @NotNull
  private int assists;
  @NotNull
  private int minutes;
  @NotNull
  private int yellowCards;
  @NotNull
  private int redCards;

  // minutesが0の場合はgoals, assists, yellowCards, redCardsも0でなければならない
  // また、starterはfalseでなければならない
  @AssertTrue
  public boolean isMinutesZero() {
    return minutes == 0 ? goals == 0 && assists == 0 && yellowCards == 0 && redCards == 0 && !starter : true;
  }
}
