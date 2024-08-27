package football.StatsManagement;

import football.StatsManagement.data.Club;
import football.StatsManagement.data.Country;
import football.StatsManagement.data.GameResult;
import football.StatsManagement.data.League;
import football.StatsManagement.data.Player;
import football.StatsManagement.data.PlayerGameStat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FootballController {
  private FootballService service;

  @Autowired
  public FootballController(FootballService service) {
    this.service = service;
  }

  @GetMapping("/countries")
  public String getCountries(Model model) {
    model.addAttribute("countries", service.getCountries());
    model.addAttribute("country", new Country());
    return "countries";
  }

  @GetMapping("/leagues/{countryId}")
  public String getLeagues(Model model, @PathVariable int countryId) {
    model.addAttribute("country", service.getCountry(countryId));
    model.addAttribute("leagues", service.getLeaguesByCountry(countryId));
    model.addAttribute("league", new League());
    return "leagues";
  }

  @GetMapping("/clubs/{leagueId}")
  public String getClubs(Model model, @PathVariable int leagueId) {
    model.addAttribute("league", service.getLeague(leagueId));
    model.addAttribute("clubs", service.getClubsByLeague(leagueId));
    model.addAttribute("club", new Club());
    return "clubs";
  }

  @GetMapping("/players/{clubId}")
  public String getPlayers(Model model, @PathVariable int clubId) {
    model.addAttribute("club", service.getClub(clubId));
    model.addAttribute("seasonStats", service.playerSeasonStats(clubId));
    model.addAttribute("player", new Player());
    return "players";
  }

  @GetMapping("/player/{playerId}")
  public String getPlayer(Model model, @PathVariable int playerId) {
    model.addAttribute("player", service.getPlayer(playerId));
    model.addAttribute("playerGameStats", service.getPlayerGameStatsByPlayer(playerId));
    model.addAttribute("newPlayerGameStat", new PlayerGameStat());
    return "player";
  }

  @PostMapping("/registerCountry")
  public String registerCountry(@ModelAttribute Country country) {
    service.registerCountry(country);
    return "redirect:/countries";
  }

  @PostMapping("/registerLeague/{countryId}")
  public String registerLeague(@ModelAttribute League league, @PathVariable int countryId) {
    league.setCountryId(countryId);
    service.registerLeague(league);
    return "redirect:/leagues/" + countryId;
  }

  @PostMapping("/registerClub/{leagueId}")
  public String registerClub(@ModelAttribute Club club, @PathVariable int leagueId) {
    club.setLeagueId(leagueId);
    service.registerClub(club);
    return "redirect:/clubs/" + leagueId;
  }

  @PostMapping("/registerPlayer/{clubId}")
  public String registerPlayer(@ModelAttribute Player player, @PathVariable int clubId) {
    player.setClubId(clubId);
    service.registerPlayer(player);
    return "redirect:/players/" + clubId;
  }

  @PostMapping("/registerPlayerGameStat/{playerId}")
  public String registerPlayerGameStat(@ModelAttribute PlayerGameStat playerGameStat, @PathVariable int playerId) {
    playerGameStat.setPlayerId(playerId);
    playerGameStat.setClubId(service.getPlayer(playerId).getClubId());
    service.registerPlayerGameStat(playerGameStat);
    return "redirect:/player/" + playerId;
  }

//  @PostMapping("/updatePlayer/{playerId}")
//  public String updatePlayer() {
//    return "redirect:/player/" + playerId;
//  }

  @PostMapping("/updatePlayerGameStat/{id}/{playerId}")
  public String updatePlayerGameStat(@ModelAttribute PlayerGameStat playerGameStat, @PathVariable int id, @PathVariable int playerId) {
    playerGameStat.setId(id);
    service.updatePlayerGameStat(playerGameStat);
    return "redirect:/player/" + playerId;
  }

  @GetMapping("/clubChoice/{leagueId}")
  public String chooseClubs(Model model, @PathVariable int leagueId) {
    model.addAttribute("leagueId", leagueId);
    model.addAttribute("clubs", service.getClubsByLeague(leagueId));

    return "clubChoice";
  }

  @GetMapping("/gameResult/{leagueId}/{homeClubId}/{awayClubId}/{gameDate}")
  public String gameResult(Model model, @PathVariable int leagueId, @PathVariable int homeClubId, @PathVariable int awayClubId, @PathVariable String gameDate) {

    model.addAttribute("leagueId", leagueId);
    model.addAttribute("gameDate", gameDate);
    model.addAttribute("homeClub", service.getClub(homeClubId));
    model.addAttribute("awayClub", service.getClub(awayClubId));
    GameResult gameResult = new GameResult();
    gameResult.setHomeClubStats(service.convertClubIdToPlayerGameStats(homeClubId));
    gameResult.setAwayClubStats(service.convertClubIdToPlayerGameStats(awayClubId));
    model.addAttribute("gameResult", gameResult);

    return "gameResult";
  }

  @PostMapping("/registerGameResult/{leagueId}/{homeClubId}/{awayClubId}/{gameDate}")
  public String registerGameResult(@ModelAttribute GameResult gameResult, @PathVariable int leagueId, @PathVariable int homeClubId, @PathVariable int awayClubId, @PathVariable String gameDate) {
//    試合結果を編集
    System.out.println("GameResult: " + gameResult);
    gameResult.setLeagueId(leagueId);
    gameResult.setHomeClubId(homeClubId);
    gameResult.setAwayClubId(awayClubId);
    gameResult.setGameDate(service.convertStringToLocalDate(gameDate));

//    登録処理
    service.registerGameResultAndPlayerGameStats(gameResult);

    return "redirect:/clubs/" + leagueId;
  }

}
