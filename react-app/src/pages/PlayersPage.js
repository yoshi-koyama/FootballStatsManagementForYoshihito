import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

function PlayersPage() {
  const { countryId } = useParams(); // URLから国IDを取得
  const { leagueId } = useParams(); // URLからリーグIDを取得
  const { clubId } = useParams(); // URLからリーグIDを取得
  const [players, setPlayers] = useState([]);
  const [newPlayerName, setNewPlayerName] = useState(''); // 新規登録用のstate
  const [number, setNumber] = useState(''); // 新規登録用のstate
  const [message, setMessage] = useState(''); // フィードバックメッセージ用のstate
  const [clubName, setClubName] = useState(''); // クラブ名を管理するstate

  useEffect(() => {
    fetch(`/countries/${countryId}/leagues/${leagueId}/clubs/${clubId}/players`) // APIエンドポイント
      .then((response) => response.json())
      .then((data) => setPlayers(data));

    fetch(`/clubs/${clubId}`) // APIエンドポイント
        .then((response) => response.json())
        .then((data) => setClubName(data.name));
  }, [clubId]);

  // フォームの入力値を管理
  const handleInputChange = (e) => {
    const { name, value } = e.target; // 変更されたフィールドのnameとvalueを取得
    if (name === 'playerName') {
      setNewPlayerName(value); // 選手名を更新
    } else if (name === 'number') {
      setNumber(value); // 背番号を更新
    }
  };

  // 新しい選手を登録する処理
  const handleFormSubmit = (e) => {
    e.preventDefault(); // ページリロードを防ぐ

    // リクエストボディを作成
    const playerForJson = {
      clubId: parseInt(clubId), // URLから取得したクラブID
      name: newPlayerName, // フォームから取得した選手名
      number: number, // フォームから取得した背番号
    };

    fetch(`/player`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json', // JSON形式で送信
      },
      body: JSON.stringify(playerForJson), // JSONとして送信
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        return response.text().then(text => { throw new Error(text); });
      })
      .then((newPlayer) => {
        setPlayers([...players, newPlayer]); // 新しいクラブをリストに追加
        setNewPlayerName(''); // 入力欄をリセット
        setMessage('Player registered successfully!');
      })
      .catch((error) => {
        setMessage('Failed to register player: ' + error.message);
        console.error(error);
      });
  };

  return (
    <div>
      {/* ClubsPageに戻るリンク */}
      <Link to={`/countries/${countryId}/leagues/${leagueId}/clubs`}>Back to Clubs</Link>
      {/* クラブ名を表示 */}
      {clubName && <h1>{clubName} Players</h1>} {/* クラブ名を表示する要素を追加 */}
      <ul>
        {players.map((player) => (
          <li key={player.id}>
            <Link to={`/countries/${countryId}/leagues/${leagueId}/clubs/${clubId}/players/${player.id}`}>{player.name}</Link>
          </li>
        ))}
      </ul>

      {/* 新しい選手の登録フォーム */}
      <h2>Register New Player</h2>
      <form onSubmit={handleFormSubmit}>
        <input
            type="number"
            name="number"
            placeholder="Number"
            value={number}
            onChange={handleInputChange}
            required
        />
        <input
          type="text"
          name='playerName'
          placeholder="Player name"
          value={newPlayerName}
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

export default PlayersPage;
