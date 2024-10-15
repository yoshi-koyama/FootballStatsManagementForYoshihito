import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useToast } from '../contexts/ToastContext';
import { getClubsByLeague, getCurrentSeason, getLeague, getSeasons, getStanding } from '../apis/GetMappings';

function ClubsPage() {
const { showToast } = useToast();

  const { countryId } = useParams(); // URLから国IDを取得
  const { leagueId } = useParams(); // URLからリーグIDを取得
  const [clubs, setClubs] = useState([]);
  const [newClubName, setNewClubName] = useState(''); // 新規登録用のstate
  const [league, setLeague] = useState(''); // リーグ名を管理するstate
  const [isStaidingView, setIsStandingView] = useState(false); // 順位表表示切り替え用のstate
  const [standing, setStanding] = useState([]);
  const [seasons, setSeasons] = useState([]);
  const [selectedSeason, setSelectedSeason] = useState(null);

  useEffect(() => {
    getSeasons(setSeasons);
    getCurrentSeason(setSelectedSeason);
  }, []);

  useEffect(() => {
    getClubsByLeague(leagueId, setClubs);
    getLeague(leagueId, setLeague);
  }, [leagueId]);

  useEffect(() => {
    if (selectedSeason) { // selectedSeasonが設定されている場合のみ実行
      getStanding(leagueId, selectedSeason.id, setStanding);
    }
  }, [leagueId, selectedSeason]);

  const handleSeasonChange = (e) => {
    const selectedSeasonId = e.target.value;
    const season = seasons.find((season) => season.id === selectedSeasonId); // idに基づいてシーズンを検索
    setSelectedSeason(season); // 選択したシーズンオブジェクトをセット
  };

  // フォームの入力値を管理
  const handleInputChange = (e) => {
    setNewClubName(e.target.value);
  };

  // 新しいクラブを登録する処理
  const handleFormSubmit = (e) => {
    e.preventDefault(); // ページリロードを防ぐ

    // リクエストボディを作成
    const clubForJson = {
      leagueId: parseInt(leagueId), // URLから取得したリーグID
      name: newClubName, // フォームから取得したクラブ名
    };

    fetch(`/club`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json', // JSON形式で送信
      },
      body: JSON.stringify(clubForJson), // JSONとして送信
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        return response.text().then((text) => { throw new Error(text); });
      })
      .then((newClub) => {
        setClubs([...clubs, newClub]); // 新しいクラブをリストに追加
        setNewClubName(''); // 入力欄をリセット
        showToast(`Club '${newClub.name}' registered successfully!`);
      })
      .catch((error) => {
        alert('Error: ' + error.message);
        console.error(error);
      });
  };

  return (
    <div>
      {/* Homeに戻るリンク */}
      <Link to="/">Home</Link>
      <br /> {/* 改行 */}
      {/* LeaguePageに戻るリンク */}
      <Link to={`/countries/${countryId}/leagues`}>Back to Leagues</Link>
      {/* リーグ名を表示 */}
      {league && <h1>{league.name} Clubs</h1>} {/* リーグ名を表示する要素を追加 */}
      {/* クラブ一覧と順位の表示切り替えボタン */}
      <button onClick={() => setIsStandingView(!isStaidingView)}>
        {isStaidingView ? 'View Clubs' : 'View Standing'}
      </button>
      <br /> {/* 改行 */}

      {/* クラブ一覧の表示 */}
      {!isStaidingView && (
        <>
          {/* 試合結果登録画面へのリンク */}
          <Link to={`/countries/${countryId}/leagues/${leagueId}/register-game-result`}>Register Game Result</Link>
          <ul>
            {clubs.map((club) => (
              <li key={club.id}>
                <Link to={`/countries/${countryId}/leagues/${leagueId}/clubs/${club.id}/players`}>{club.name}</Link>
              </li>
            ))}
          </ul>

          {/* 新しいクラブの登録フォーム */}
          <h2>Register New Club</h2>
          <form onSubmit={handleFormSubmit}>
            <input
              type="text"
              placeholder="Club name"
              value={newClubName}
              onChange={handleInputChange}
              required
            />
            <button type="submit">Register</button>
          </form>
        </>
      )}

      {/* 順位表の表示 */}
      {isStaidingView && (
        <>
          <label htmlFor="season-select">Choose a season:</label>
            <select id="season-select" value={selectedSeason?.id || ''} onChange={handleSeasonChange}>
              {seasons.map((season) => (
                <option key={season.id} value={season.id}>
                  {season.name}
                </option>
              ))}
            </select>

          <table>
            <thead>
              <tr>
                <th>Position</th>
                <th>Club</th>
              </tr>
            </thead>
            <tbody>
              {standing.clubForStandings.map((clubForStanding) => (
                <tr key={clubForStanding.club.id}>
                  <td>{clubForStanding.position}</td>
                  <td>
                    <Link to={`/countries/${countryId}/leagues/${leagueId}/clubs/${clubForStanding.club.id}/players`}>
                      {clubForStanding.club.name}
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}

    </div>
  );
}

export default ClubsPage;
