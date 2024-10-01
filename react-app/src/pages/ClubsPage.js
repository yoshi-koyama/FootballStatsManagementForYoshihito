import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

function ClubsPage() {
  const { countryId } = useParams(); // URLから国IDを取得
  const { leagueId } = useParams(); // URLからリーグIDを取得
  const [clubs, setClubs] = useState([]);
  const [newClubName, setNewClubName] = useState(''); // 新規登録用のstate
  const [message, setMessage] = useState(''); // フィードバックメッセージ用のstate
  const [leagueName, setLeagueName] = useState(''); // リーグ名を管理するstate

  useEffect(() => {
    fetch(`/leagues/${leagueId}/clubs`) // APIエンドポイント
      .then((response) => response.json())
      .then((data) => setClubs(data));

    fetch(`/leagues/${leagueId}`) // APIエンドポイント
        .then((response) => response.json())
        .then((data) => setLeagueName(data.name));
  }, [leagueId]);

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
    </div>
  );
}

export default ClubsPage;
