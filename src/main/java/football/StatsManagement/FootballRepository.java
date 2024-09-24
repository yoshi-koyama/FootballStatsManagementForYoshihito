package football.StatsManagement;

import football.StatsManagement.model.data.Club;
import football.StatsManagement.model.data.Country;
import football.StatsManagement.model.data.GameResult;
import football.StatsManagement.model.data.League;
import football.StatsManagement.model.data.Player;
import football.StatsManagement.model.data.PlayerGameStat;
import football.StatsManagement.model.data.Season;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FootballRepository {
//  Insert

  /**
   * Insert a country
   * @param country
   */
  @Insert("INSERT INTO countries (name) VALUES (#{name})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertCountry(Country country);

  /**
   * Insert a league
   * @param league
   */
  @Insert("INSERT INTO leagues (name, country_id) VALUES (#{name}, #{countryId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertLeague(League league);

  /**
   * Insert a club
   * @param club
   */
  @Insert("INSERT INTO clubs (name, league_id) VALUES (#{name}, #{leagueId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertClub(Club club);

  /**
   * Insert a player
   * @param player
   */
  @Insert("INSERT INTO players (number, name, club_id) VALUES (#{number}, #{name}, #{clubId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertPlayer(Player player);

  /**
   * Insert a player game stat
   * @param playerGameStats
   */
  @Insert("INSERT INTO player_game_stats (player_id, club_id, starter, goals, assists, minutes, yellow_cards, red_cards, game_date) VALUES (#{playerId}, #{clubId}, #{starter}, #{goals}, #{assists}, #{minutes}, #{yellowCards}, #{redCards}, #{gameDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertPlayerGameStat(PlayerGameStat playerGameStats);

  /**
   * Insert a game result
   * @param gameResult
   */
  @Insert("INSERT INTO game_results (home_club_id, away_club_id, home_score, away_score, winner_club_id, league_id, game_date) VALUES (#{homeClubId}, #{awayClubId}, #{homeScore}, #{awayScore}, #{winnerClubId}, #{leagueId}, #{gameDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertGameResult(GameResult gameResult);

  /**
   * Insert a season
   * @param season
   */
  @Insert("INSERT INTO seasons (name, start_date, end_date) VALUES (#{name}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertSeason(Season season);

//  Select
  /**
   * Select a country
   * @param id
   * @return
   */
  @Select("SELECT * FROM countries WHERE id = #{id}")
  Country selectCountry(int id);

  /**
   * Select a league
   * @param id
   * @return
   */
  @Select("SELECT * FROM leagues WHERE id = #{id}")
  League selectLeague(int id);

  /**
   * Select a club
   * @param id
   * @return
   */
  @Select("SELECT * FROM clubs WHERE id = #{id}")
  Club selectClub(int id);

  /**
   * Select a player
   * @param id
   * @return
   */
  @Select("SELECT * FROM players WHERE id = #{id}")
  Player selectPlayer(int id);

  /**
   * Select a player game stat
   * @param id
   * @return
   */
  @Select("SELECT * FROM player_game_stats WHERE id = #{id}")
  PlayerGameStat selectPlayerGameStat(int id);

  /**
   * Select game results by league and season
   * @param seasonId
   * @param clubId
   * @return
   */
  @Select("SELECT * FROM game_results WHERE season_id = #{seasonId} AND (home_club_id = #{clubId} OR away_club_id = #{clubId})")
  List<GameResult> selectGameResultsByClubAndSeason(int seasonId, int clubId);

  /**
   * Select game result
   * @param id
   * @return
   */
  @Select("SELECT * FROM game_results WHERE id = #{id}")
  GameResult selectGameResult(int id);

  /**
   * Select player game stats by player
   * @param playerId
   * @return
   */
  @Select("SELECT * FROM player_game_stats WHERE player_id = #{playerId}")
  List<PlayerGameStat> selectPlayerGameStatsByPlayer(int playerId);

  /**
   * Select players by club
   * @param clubId
   * @return
   */
  @Select("SELECT * FROM players WHERE club_id = #{clubId}")
  List<Player> selectPlayersByClub(int clubId);

  /**
   * Select clubs by league
   * @param leagueId
   * @return
   */
  @Select("SELECT * FROM clubs WHERE league_id = #{leagueId}")
  List<Club> selectClubsByLeague(int leagueId);

  /**
   * Select leagues by country
   * @param countryId
   * @return
   */
  @Select("SELECT * FROM leagues WHERE country_id = #{countryId}")
  List<League> selectLeaguesByCountry(int countryId);

  /**
   * Select all countries
   * @return
   */
  @Select("SELECT * FROM countries")
  List<Country> selectCountries();

  /**
   * Select all seasons
   * @return
   */
  @Select("SELECT * FROM seasons")
  List<Season> selectSeasons();

//  update
  /**
   * Update a player
   * @param player
   */
  @Update("UPDATE players SET club_id = #{clubId}, name = #{name} WHERE id = #{id}")
  void updatePlayer(Player player);

}
