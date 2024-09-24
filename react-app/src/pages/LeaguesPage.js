import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

function LeaguesPage() {
  const { countryId } = useParams(); // URLから国IDを取得
  const [leagues, setLeagues] = useState([]);
  const [newLeagueName, setNewLeagueName] = useState(''); // 新規登録用のstate
  const [message, setMessage] = useState(''); // フィードバックメッセージ用のstate
  const [countryName, setCountryName] = useState(''); // 国名を管理するstate

  useEffect(() => {
    fetch(`/countries/${countryId}/leagues`) // APIエンドポイント
      .then((response) => response.json())
      .then((data) => setLeagues(data));

    fetch(`/countries/${countryId}`) // APIエンドポイント
        .then((response) => response.json())
        .then((data) => setCountryName(data.name));
  }, [countryId]);

  // フォームの入力値を管理
  const handleInputChange = (e) => {
    setNewLeagueName(e.target.value);
  };

  // 新しいリーグを登録する処理
  const handleFormSubmit = (e) => {
    e.preventDefault(); // ページリロードを防ぐ

    // リクエストボディを作成
    const leagueForJson = {
        countryId: parseInt(countryId), // URLから取得した国ID
        name: newLeagueName, // フォームから取得したリーグ名
      };

      fetch(`/league`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json', // JSON形式で送信
        },
        body: JSON.stringify(leagueForJson), // JSONとして送信
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          }
          throw new Error('Failed to register league');
        })
        .then((newLeague) => {
          setLeagues([...leagues, newLeague]); // 新しいリーグをリストに追加
          setNewLeagueName(''); // 入力欄をリセット
          setMessage('League registered successfully!');
        })
        .catch((error) => {
          setMessage('Failed to register league.');
          console.error(error);
        });
  };

  return (
    <div>
      {/* CountriesPageに戻るリンク */}
      <Link to="/">Back to Countries</Link>
      {/* 国名を表示 */}
      {countryName && <h1>{countryName} Leagues</h1>} {/* 国名を表示する要素を追加 */}
      <ul>
        {leagues.map((league) => (
          <li key={league.id}>
            <Link to={`/countries/${countryId}/leagues/${league.id}/clubs`}>{league.name}</Link>
          </li>
        ))}
      </ul>

      {/* 新しいリーグの登録フォーム */}
      <h2>Register New League</h2>
      <form onSubmit={handleFormSubmit}>
        <input
          type="text"
          placeholder="League name"
          value={newLeagueName}
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

export default LeaguesPage;
