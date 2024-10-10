import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

function ClubsPage() {
  const { countryId } = useParams(); // URLから国IDを取得
  const { leagueId } = useParams(); // URLからリーグIDを取得
  const [clubs, setClubs] = useState([]);
  const [newClubName, setNewClubName] = useState(''); // 新規登録用のstate
  const [message, setMessage] = useState(''); // フィードバックメッセージ用のstate
  const [leagueName, setLeagueName] = useState(''); // リーグ名を管理するstate
  const [isStaidingView, setIsStandingView] = useState(false); // 順位表表示切り替え用のstate
  const [standing, setStanding] = useState([]);
  const [seasons, setSeasons] = useState([]);
  const [selectedSeason, setSelectedSeason] = useState(null);

  useEffect(() => {
    fetch(`/seasons`) // APIエンドポイント
      .then((response) => response.json())
      .then((data) => setSeasons(data));

    fetch('/seasons/current') // APIエンドポイント
      .then((response) => response.json())
      .then((data) => setSelectedSeason(data));

  }, []);

  useEffect(() => {
    fetch(`/leagues/${leagueId}/clubs`) // APIエンドポイント
      .then((response) => response.json())
      .then((data) => setClubs(data));

    fetch(`/leagues/${leagueId}`) // APIエンドポイント
        .then((response) => response.json())
        .then((data) => setLeagueName(data.name));
  }, [leagueId]);

  useEffect(() => {
    if (selectedSeason) { // selectedSeasonが設定されている場合のみ実行
      fetch(`/leagues/${leagueId}/standings/${selectedSeason.id}`) // APIエンドポイント
        .then((response) => response.json())
        .then((data) => setStanding(data));
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
        throw new Error('Failed to register club');
      })
      .then((newClub) => {
        setClubs([...clubs, newClub]); // 新しいクラブをリストに追加
        setNewClubName(''); // 入力欄をリセット
        setMessage('Club registered successfully!');
      })
      .catch((error) => {
        setMessage('Failed to register club.');
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
      {leagueName && <h1>{leagueName} Clubs</h1>} {/* リーグ名を表示する要素を追加 */}
      {/* クラブ一覧と順位の表示切り替えボタン */}
      <button onClick={() => setIsStandingView(!isStaidingView)}>
        {isStaidingView ? 'View Clubs' : 'View Standing'}
      </button>
      <br /> {/* 改行 */}

      {/* クラブ一覧の表示 */}
      {!isStaidingView && (
        <>
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

          {/* 登録結果のフィードバックメッセージ */}
          {message && <p>{message}</p>}
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
