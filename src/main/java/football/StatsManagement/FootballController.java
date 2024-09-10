package football.StatsManagement;

import football.StatsManagement.data.Club;
import football.StatsManagement.data.Country;
import football.StatsManagement.data.GameResult;
import football.StatsManagement.data.League;
import football.StatsManagement.data.Player;
import football.StatsManagement.data.PlayerGameStat;
import football.StatsManagement.data.Season;
import football.StatsManagement.domain.GameResultForInsert;
import football.StatsManagement.domain.GameResultWithPlayerStats;
import football.StatsManagement.domain.PlayerFortInsert;
import football.StatsManagement.domain.PlayerGameStatForInsert;
import football.StatsManagement.domain.Standing;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class FootballController {
  private FootballService service;

  @Autowired
  public FootballController(FootballService service) {
    this.service = service;
  }

  @GetMapping("/countries")
  public List<Country> getCountries() {
    return service.getCountries();
  }

  @GetMapping("/leagues/{countryId}")
  public List<League> getLeagues(@PathVariable @Positive int countryId) {
    return service.getLeaguesByCountry(countryId);
  }

  @GetMapping("/clubs/{leagueId}")
  public List<Club> getClubs(@PathVariable @Positive int leagueId) {
    return service.getClubsByLeague(leagueId);
  }

  @GetMapping("/standing/{leagueId}/{seasonId}")
  public Standing getStanding(@PathVariable @Positive int leagueId, @PathVariable @Positive int seasonId) {
    return Standing.initialStanding(leagueId, seasonId, service);
  }

  @GetMapping("/players/{clubId}")
  public List<Player> getPlayers(@PathVariable @Positive int clubId) {
    return service.getPlayersByClub(clubId);
  }

  @GetMapping("/player/{playerId}")
  public Player getPlayer(@PathVariable @Positive int playerId) {
    return service.getPlayer(playerId);
  }

  @PostMapping("/registerCountry")
  public ResponseEntity<Country> registerCountry(@RequestParam @NotBlank String name) {
    Country country = new Country(name);
    service.registerCountry(country);

    return ResponseEntity.ok().body(country);
  }

  @PostMapping("/registerLeague/{countryId}")
  public ResponseEntity<League> registerLeague(@PathVariable @Positive int countryId, @RequestParam @NotBlank String name) {
    League league = new League(countryId, name);
    service.registerLeague(league);

    return ResponseEntity.ok().body(league);
  }

  @PostMapping("/registerClub/{leagueId}")
  public ResponseEntity<Club> registerClub(@PathVariable @Positive int leagueId, @RequestParam @NotBlank String name) {
    Club club = new Club(leagueId, name);
    service.registerClub(club);

    return ResponseEntity.ok().body(club);
  }

  @PostMapping("/registerPlayer/{clubId}")
  public ResponseEntity<Player> registerPlayer(@PathVariable @Positive int clubId, @RequestBody
      PlayerFortInsert playerFortInsert) {
    Player player = new Player(playerFortInsert);
    service.registerPlayer(player);

    return ResponseEntity.ok().body(player);
  }

  @PostMapping("/registerPlayerGameStat/{playerId}")
  public ResponseEntity<PlayerGameStat> registerPlayerGameStat(@PathVariable @Positive int playerId, @RequestBody PlayerGameStat playerGameStat) {
    playerGameStat.setClubId(service.getPlayer(playerId).getClubId());
    playerGameStat.setNumber(service.getPlayer(playerId).getNumber());
    service.registerPlayerGameStat(playerGameStat);

    return ResponseEntity.ok().body(playerGameStat);
  }

//  @PostMapping("/updatePlayer/{playerId}")
//  public String updatePlayer() {
//    return "redirect:/player/" + playerId;
//  }

  @PostMapping("/registerGameResult/{leagueId}/{homeClubId}/{awayClubId}")
  public ResponseEntity<GameResultWithPlayerStats> registerGameResult(
      @RequestBody @Valid GameResultForInsert gameResultForInsert,
      @RequestBody @Valid List<PlayerGameStatForInsert> homeClubStatsForInsert,
      @RequestBody @Valid List<PlayerGameStatForInsert> awayClubStatsForInsert,
      @PathVariable @Positive int leagueId, @PathVariable @Positive int homeClubId, @PathVariable @Positive int awayClubId)
      throws FootballException {
    GameResult gameResult = new GameResult(gameResultForInsert);
    List<PlayerGameStat> homeClubStats = service.convertPlayerGameStatsForInsertToPlayerGameStats(homeClubStatsForInsert);
    List<PlayerGameStat> awayClubStats = service.convertPlayerGameStatsForInsertToPlayerGameStats(awayClubStatsForInsert);
    GameResultWithPlayerStats gameResultWithPlayerStats = new GameResultWithPlayerStats(gameResult, homeClubStats, awayClubStats);

    service.registerGameResultAndPlayerGameStats(gameResultWithPlayerStats);

    return ResponseEntity.ok().body(gameResultWithPlayerStats);
  }

  @PostMapping("/registerSeason")
  public ResponseEntity<Season> registerSeason(@RequestBody @Valid Season season) throws FootballException {
    service.registerSeason(season);

    return ResponseEntity.ok().body(season);
  }

  @PatchMapping("/updatePlayer/{playerId}")
  public ResponseEntity<Player> updatePlayer(@PathVariable @Positive int playerId, @RequestBody Player player) {
    service.updatePlayer(player);

    return ResponseEntity.ok().body(player);
  }


}
