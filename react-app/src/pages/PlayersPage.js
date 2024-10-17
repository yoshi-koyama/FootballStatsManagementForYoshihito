import React, { useEffect, useState, useRef } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useToast } from '../contexts/ToastContext';
import { getClub, getCurrentSeason, getPlayersByClub, getPlayersSeasonStatsByClub } from '../apis/GetMappings.js';

function PlayersPage() {
  const { showToast } = useToast();
  const numberInputRef = useRef(null); // 背番号入力欄の参照を作成

  const { countryId } = useParams(); // URLから国IDを取得
  const { leagueId } = useParams(); // URLからリーグIDを取得
  const { clubId } = useParams(); // URLからリーグIDを取得
  const [players, setPlayers] = useState([]);
  const [newPlayerName, setNewPlayerName] = useState(''); // 新規登録用のstate
  const [number, setNumber] = useState(''); // 新規登録用のstate
  const [club, setClub] = useState(''); // クラブ名を管理するstate
  const [playerSeasonStats, setPlayerSeasonStats] = useState([]); // 選手たちのシーズン成績を管理するstate
  const [currentSeason, setCurrentSeason] = useState(null); // 現在のシーズンを管理するstate

  useEffect(() => {
    getCurrentSeason(setCurrentSeason);
  }, []);

  useEffect(() => {
    getPlayersByClub(clubId, setPlayers);
    getClub(clubId, setClub);
  }, [clubId]);

  useEffect(() => {
    if (currentSeason) { // currentSeasonが設定されている場合のみ実行
      getPlayersSeasonStatsByClub(clubId, currentSeason.id, setPlayerSeasonStats);
    }
  }, [clubId, currentSeason]);

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
        setNumber((prevNumber) => {
          const nextNumber = String(parseInt(prevNumber) + 1);
          return nextNumber;
        });
        showToast(`Player '${newPlayer.name}' registered successfully!`);
        // フォーカスを当て、選択状態にする
        if (numberInputRef.current) {
          numberInputRef.current.focus();
          numberInputRef.current.select();
        }
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
      {/* ClubsPageに戻るリンク */}
      <Link to={`/countries/${countryId}/leagues/${leagueId}/clubs`}>Back to Clubs</Link>
      {/* クラブ名を表示 */}
      {club && <h1>{club.name} Players</h1>} {/* クラブ名を表示する要素を追加 */}
      <table>
        <thead>
          <tr>
            <th>Number</th>
            <th>Name</th>
            <th>Games</th>
            <th>Goals</th>
          </tr>
        </thead>
        <tbody>
          {players.map((player) => (
            <tr key={player.id}>
              <td>{player.number}</td>
              <td>
                <Link to={`/countries/${countryId}/leagues/${leagueId}/clubs/${clubId}/players/${player.id}`}>
                  {player.name}
                </Link>
              </td>
              <td>{playerSeasonStats.find((stats) => stats.playerId === player.id)?.games}</td>
              <td>{playerSeasonStats.find((stats) => stats.playerId === player.id)?.goals}</td>
            </tr>
          ))}
        </tbody>
      </table>

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
            ref={numberInputRef}
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
    </div>
  );
}

export default PlayersPage;
