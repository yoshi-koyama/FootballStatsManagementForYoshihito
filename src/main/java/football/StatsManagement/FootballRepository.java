package football.StatsManagement;

import football.StatsManagement.data.Club;
import football.StatsManagement.data.Country;
import football.StatsManagement.data.GameResult;
import football.StatsManagement.data.League;
import football.StatsManagement.data.Player;
import football.StatsManagement.data.PlayerGameStat;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FootballRepository {
//  Insert
  @Insert("INSERT INTO countries (name) VALUES (#{name})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertCountry(Country country);

  @Insert("INSERT INTO leagues (name, country_id) VALUES (#{name}, #{countryId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertLeague(League league);

  @Insert("INSERT INTO clubs (name, league_id) VALUES (#{name}, #{leagueId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertClub(Club club);

  @Insert("INSERT INTO players (name, club_id) VALUES (#{name}, #{clubId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertPlayer(Player player);

  @Insert("INSERT INTO player_game_stats (player_id, club_id, starter, goals, assists, minutes, yellow_cards, red_cards, game_date) VALUES (#{playerId}, #{clubId}, #{starter}, #{goals}, #{assists}, #{minutes}, #{yellowCards}, #{redCards}, #{gameDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertPlayerGameStat(PlayerGameStat playerGameStats);

  @Insert("INSERT INTO game_results (home_club_id, away_club_id, home_score, away_score, winner_club_id, league_id, game_date) VALUES (#{homeClubId}, #{awayClubId}, #{homeScore}, #{awayScore}, #{winnerClubId}, #{leagueId}, #{gameDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertGameResult(GameResult gameResult);

//  Select
  @Select("SELECT * FROM countries WHERE id = #{id}")
  Country selectCountry(int id);

  @Select("SELECT * FROM leagues WHERE id = #{id}")
  League selectLeague(int id);

  @Select("SELECT * FROM clubs WHERE id = #{id}")
  Club selectClub(int id);

  @Select("SELECT * FROM players WHERE id = #{id}")
  Player selectPlayer(int id);

  @Select("SELECT * FROM player_game_stats WHERE id = #{id}")
  PlayerGameStat selectPlayerGameStat(int id);

  @Select("SELECT * FROM player_game_stats WHERE player_id = #{playerId}")
  List<PlayerGameStat> selectPlayerGameStatsByPlayer(int playerId);

  @Select("SELECT * FROM players WHERE club_id = #{clubId}")
  List<Player> selectPlayersByClub(int clubId);

  @Select("SELECT * FROM clubs WHERE league_id = #{leagueId}")
  List<Club> selectClubsByLeague(int leagueId);

  @Select("SELECT * FROM leagues WHERE country_id = #{countryId}")
  List<League> selectLeaguesByCountry(int countryId);

  @Select("SELECT * FROM countries")
  List<Country> selectCountries();

//  update
  @Update("UPDATE players SET club_id = #{clubId}, name = #{name} WHERE id = #{id}")
  void updatePlayer(Player player);

  @Update("UPDATE player_game_stats SET starter = #{starter}, goals = #{goals}, assists = #{assists}, minutes = #{minutes}, yellow_cards = #{yellowCards}, red_cards = #{redCards} WHERE id = #{id}")
  void updatePlayerGameStat(PlayerGameStat playerGameStat);
}
