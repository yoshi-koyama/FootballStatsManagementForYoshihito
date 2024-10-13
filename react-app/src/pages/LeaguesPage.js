import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { toast } from 'react-toastify'; // トースト通知を追加
import 'react-toastify/dist/ReactToastify.css'; // トーストのスタイル
import { getCountry, getLeaguesByCountry } from '../apis/GetMappings';

function LeaguesPage() {
  const { countryId } = useParams(); // URLから国IDを取得
  const [leagues, setLeagues] = useState([]);
  const [newLeagueName, setNewLeagueName] = useState(''); // 新規登録用のstate
  const [country, setCountry] = useState([]); // 国を管理するstate

  useEffect(() => {
    getLeaguesByCountry(countryId, setLeagues);
    getCountry(countryId, setCountry);
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
          return response.text().then((text) => { throw new Error(text); });
        })
        .then((newLeague) => {
          setLeagues([...leagues, newLeague]); // 新しいリーグをリストに追加
          setNewLeagueName(''); // 入力欄をリセット
          toast.success(`League '${newLeague.name}' registered successfully!`);
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
      {/* CountriesPageに戻るリンク */}
      <Link to="/countries">Back to Countries</Link>
      {/* 国名を表示 */}
      {country && <h1>{country.name} Leagues</h1>} {/* 国名を表示する要素を追加 */}
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
    </div>
  );
}

export default LeaguesPage;
